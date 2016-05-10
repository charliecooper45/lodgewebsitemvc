package uk.cooperca.lodge.website.mvc.command.constraint.validator;

import uk.cooperca.lodge.website.mvc.command.constraint.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.lang.Character.*;

/**
 * {@link ConstraintValidator} implementation for the {@link Password} annotation.
 *
 * @author Charlie Cooper
 */
public class PasswordValidator implements ConstraintValidator<Password, Object> {

    @Override
    public void initialize(Password constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String password = (String) value;
        int upper = 0;
        int digit = 0;

        for (char c : password.toCharArray()) {
            if (isSpaceChar(c)) {
                return false;
            } else if (isUpperCase(c)) {
                upper++;
            } else if (isDigit(c)) {
                digit++;
            }
        }

        if (upper >= 1 && digit >= 1) {
            return true;
        }
        return false;
    }
}
