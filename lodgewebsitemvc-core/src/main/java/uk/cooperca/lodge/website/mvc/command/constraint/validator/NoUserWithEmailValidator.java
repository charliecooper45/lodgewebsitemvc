package uk.cooperca.lodge.website.mvc.command.constraint.validator;

import org.springframework.beans.factory.annotation.Autowired;
import uk.cooperca.lodge.website.mvc.command.constraint.NoUserWithEmail;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * {@link ConstraintValidator} implementation for the {@link NoUserWithEmail} annotation.
 *
 * @author Charlie Cooper
 */
public class NoUserWithEmailValidator implements ConstraintValidator<NoUserWithEmail, String> {

    @Autowired
    private UserService service;

    @Override
    public void initialize(NoUserWithEmail constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<User> user = service.getUserByEmail(email);
        return !user.isPresent();
    }
}
