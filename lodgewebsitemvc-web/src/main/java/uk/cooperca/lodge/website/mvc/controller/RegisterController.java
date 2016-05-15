package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.cooperca.lodge.website.mvc.command.RegisterCommand;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

/**
 * Controller that registers users on the platform.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping
    public String register(Model model) {
        model.addAttribute("registerCommand", new RegisterCommand());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doRegister(Model model, @Valid RegisterCommand command, BindingResult result, Locale locale) {
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

        User user = userService.registerUser(command);
        // log the new user in
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/register/success";
    }

    @RequestMapping("/success")
    public String registerSuccess() {
        return "registerSuccess";
    }
}
