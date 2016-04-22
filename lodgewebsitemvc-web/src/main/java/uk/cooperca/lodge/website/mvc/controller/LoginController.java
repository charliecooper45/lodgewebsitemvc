package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for all login related operations.
 *
 * @author Charlie Cooper
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
