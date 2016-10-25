package uk.cooperca.lodge.website.mvc.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.cooperca.lodge.website.mvc.command.PasswordCommand;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.security.session.SessionManager;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Optional;

/**
 * Controller for operations on users that do not require authentication.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    @RequestMapping(value = "/verify")
    public String verifyUser(@RequestParam("token") String token, Model model) {
        try {
            User user = userService.verifyUser(token);
            if (user != null) {
                if (isAuthenticated()) {
                    setCurrentUser(user);
                } else {
                    sessionManager.expireUserSession(user.getId());
                }
                return "redirect:/user/verificationSuccess";
            }
            model.addAttribute("error", "Unable to verify your account, maybe you are already verified?");
        } catch (MalformedJwtException | SignatureException e) {
            model.addAttribute("error", "Invalid verification link");
        } catch (ExpiredJwtException e) {
            model.addAttribute("error", "Verification link has expired");
        }
        return "verificationFailure";
    }

    @RequestMapping("/verificationSuccess")
    public String verificationSuccess() {
        return "verificationSuccess";
    }

    @RequestMapping("/forgottenPassword")
    public String forgottenPassword(Model model) {
        model.addAttribute("passwordCommand", new PasswordCommand());
        return "forgottenPassword";
    }

    @RequestMapping(value = "/requestPassword", method = RequestMethod.POST)
    public String requestPassword(@Validated PasswordCommand command, BindingResult result) {
        if (result.hasErrors()) {
            return "forgottenPassword";
        }
        Optional<User> user = userService.getUserByEmail(command.getEmail());
        userService.sendPasswordResetRequest(user.get().getId());
        return "passwordRequested";
    }
}
