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

    private int min;
    private int max;

    @Override
    public void initialize(Password password) {
        this.min = password.min();
        this.max = password.max();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String password = (String) value;

        // correct length
        if (password.length() < min || password.length() > max) {
            return false;
        }

        // one upper case character and one digit
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
