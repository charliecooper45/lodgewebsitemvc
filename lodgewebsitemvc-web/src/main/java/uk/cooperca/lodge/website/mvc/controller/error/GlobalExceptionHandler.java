package uk.cooperca.lodge.website.mvc.controller.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for our controllers.
 *
 * @author Charlie Cooper
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleError() {
        return "error";
    }
}
