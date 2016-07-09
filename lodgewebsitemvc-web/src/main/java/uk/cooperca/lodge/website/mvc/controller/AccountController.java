package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.command.constraint.group.UserValidationGroups.EmailValidationGroup;
import uk.cooperca.lodge.website.mvc.command.constraint.group.UserValidationGroups.FirstNameValidationGroup;
import uk.cooperca.lodge.website.mvc.command.constraint.group.UserValidationGroups.LastNameValidationGroup;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.List;
import java.util.Locale;

import static uk.cooperca.lodge.website.mvc.controller.AccountController.UpdateField.EMAIL;
import static uk.cooperca.lodge.website.mvc.controller.AccountController.UpdateField.FIRST_NAME;
import static uk.cooperca.lodge.website.mvc.controller.AccountController.UpdateField.LAST_NAME;


/**
 * Controller for operations related to a user's account.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/account")
public class AccountController extends AbstractController {

    // supported fields for updates
    public enum UpdateField {
        EMAIL("register.email"),
        FIRST_NAME("register.firstName"),
        LAST_NAME("register.lastName");

        private final String messageCode;

        UpdateField(String messageCode) {
            this.messageCode = messageCode;
        }

        public String getMessageCode() {
            return messageCode;
        }
    }

    @Autowired
    private UserService userService;

    @RequestMapping
    public String showAccount(Model model) {
        model.addAttribute("user", getCurrentUser());
        return "account";
    }

    @RequestMapping(value = "/verification")
    public String requestVerificationEmail() {
        userService.requestVerificationEmail(getCurrentUser().getId());
        return "verificationRequested";
    }

    @RequestMapping(value = "/email", method = RequestMethod.PUT)
    public ResponseEntity<List<String>> updateEmail(@Validated(EmailValidationGroup.class) @RequestBody UserCommand command,
                                                    BindingResult result, Locale locale) {
        return handleUpdate(EMAIL, command, result, locale);
    }

    @RequestMapping(value = "/firstname", method = RequestMethod.PUT)
    public ResponseEntity<List<String>> updateFirstName(@Validated(FirstNameValidationGroup.class) @RequestBody UserCommand command,
                                                        BindingResult result, Locale locale) {
        return handleUpdate(FIRST_NAME, command, result, locale);
    }

    @RequestMapping(value = "/lastname", method = RequestMethod.PUT)
    public ResponseEntity<List<String>> updateLastName(@Validated(LastNameValidationGroup.class) @RequestBody UserCommand command,
                                                       BindingResult result, Locale locale) {
        return handleUpdate(LAST_NAME, command, result, locale);
    }

    /**
     * Method that handles updates to a user's account and returns a response with the body either set to a success message
     * or a list of errors.
     *
     * @param field the field to be updated
     * @param command the command sent to the API
     * @param result the binding result
     * @param locale the user's locale
     *
     * @return the response
     */
    private ResponseEntity handleUpdate(UpdateField field, UserCommand command, BindingResult result, Locale locale) {
        if (result.hasErrors()) {
            return errorResponse(result, locale);
        }
        User user = getCurrentUser();
        String email = user.getEmail();
        switch (field) {
            case EMAIL:
                // we must lookup using the new email address
                email = command.getEmail();
                userService.updateEmail(command.getEmail(), user.getId());
                break;
            case FIRST_NAME:
                userService.updateFirstName(command.getFirstName(), user.getId());
                break;
            case LAST_NAME:
                userService.updateLastName(command.getLastName(), user.getId());
                break;
        }
        setCurrentUser(userService.getUserByEmail(email).get());
        return successResponse("account.updateSuccess",
                new String[]{messageSource.getMessage(field.getMessageCode(), null, locale)}, locale);
    }
}
