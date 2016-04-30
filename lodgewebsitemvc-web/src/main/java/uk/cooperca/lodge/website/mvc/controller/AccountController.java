package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.cooperca.lodge.website.mvc.entity.User;

/**
 * Controller for operations related to a user's account.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @RequestMapping
    public String showAccount() {
        // TODO
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "account";
    }
}
