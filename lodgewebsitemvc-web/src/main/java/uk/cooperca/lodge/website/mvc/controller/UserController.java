package uk.cooperca.lodge.website.mvc.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.security.session.SessionManager;
import uk.cooperca.lodge.website.mvc.service.UserService;

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
        // TODO: we should attach the locale to the link we send to the user so we can return the correct error messages here
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
}
