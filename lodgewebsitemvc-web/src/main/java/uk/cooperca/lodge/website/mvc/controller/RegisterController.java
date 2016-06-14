package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Locale;
import java.util.Optional;

/**
 * Controller that registers users on the platform.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/register")
public class RegisterController extends AbstractController {

    @Autowired
    private UserService userService;

    @RequestMapping
    public String register(Model model) {
        model.addAttribute("userCommand", new UserCommand());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doRegister(Model model, @Validated UserCommand command, BindingResult result, Locale locale) {
        if (result.hasErrors()) {
            return "register";
        }

        // first we check that a user with this email address is not registered
        Optional<User> existingUser = userService.getUserByEmail(command.getEmail());
        if (existingUser.isPresent()) {
            String message = messageSource.getMessage("registerFailure.emailInUse", new Object[]{command.getEmail()}, locale);
            model.addAttribute("errorText", message);
            return "registerFailure";
        }

        // log the new user in
        setCurrentUser(userService.registerUser(command, locale));
        return "redirect:/register/success";
    }

    @RequestMapping("/success")
    public String registerSuccess() {
        return "registerSuccess";
    }
}
