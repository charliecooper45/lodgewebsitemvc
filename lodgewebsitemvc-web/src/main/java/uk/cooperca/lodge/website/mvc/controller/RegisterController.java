package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.cooperca.lodge.website.mvc.command.RegisterCommand;

import javax.validation.Valid;

/**
 * Controller that registers users on the platform.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    @RequestMapping
    public String register(Model model) {
        model.addAttribute("registerCommand", new RegisterCommand());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doRegister(@Valid RegisterCommand command, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        return "registerSuccess";
    }
}
