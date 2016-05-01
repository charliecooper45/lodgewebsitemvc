package uk.cooperca.lodge.website.mvc.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.cooperca.lodge.website.mvc.command.RegisterCommand;
import uk.cooperca.lodge.website.mvc.entity.Role;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.repository.RoleRepository;
import uk.cooperca.lodge.website.mvc.repository.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName.ROLE_USER;

/**
 * Controller that registers users on the platform.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping
    public String register(Model model) {
        model.addAttribute("registerCommand", new RegisterCommand());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doRegister(Model model, @Valid RegisterCommand command, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        // we default users to the user role
        Optional<User> existingUser = userRepository.findByEmail(command.getEmail());
        if (existingUser.isPresent()) {
            // TODO: externalise message
            model.addAttribute("errorText", "Email address " + command.getEmail() + " is already in use.");
            return "registerFailure";
        }

        Role role = roleRepository.findByRoleName(ROLE_USER);
        User user = new User(command.getEmail(), command.getPassword(), command.getFirstName(), command.getLastName(),
                role, DateTime.now());
        user = userRepository.save(user);

        // log the new user in
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "registerSuccess";
    }
}
