package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import uk.cooperca.lodge.website.mvc.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Abstract superclass for all controllers.
 *
 * @author Charlie Cooper
 */
public class AbstractController {

    @Autowired
    protected MessageSource messageSource;

    protected User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected void setCurrentUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected ResponseEntity errorResponse(BindingResult result, Locale locale) {
        List<String> errors = result.getAllErrors().stream()
                .map(error -> messageSource.getMessage(error, locale))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(errors);
    }

    public ResponseEntity successResponse(String code, String[] args, Locale locale) {
        String message = messageSource.getMessage(code, args, locale);
        return ResponseEntity
                .ok()
                .contentType(APPLICATION_JSON)
                .body(Collections.singletonList(message));
    }

}
