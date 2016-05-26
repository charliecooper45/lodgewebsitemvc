package uk.cooperca.lodge.website.mvc.command.constraint.validator;

import org.junit.Test;
import uk.cooperca.lodge.website.mvc.command.constraint.Password;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordValidatorTest {

    @Test
    public void test() {
        // we allow passwords between 5 and 15 characters
        PasswordValidator validator = new PasswordValidator();
        Password password = mock(Password.class);
        when(password.min()).thenReturn(5);
        when(password.max()).thenReturn(15);
        validator.initialize(password);

        // incorrect sizes
        assertFalse(validator.isValid("1234", null));
        assertFalse(validator.isValid("eqs123Pasfewr2s1", null));

        // passwords cannot contain space
        assertFalse(validator.isValid("1Passwor d", null));

        // passwords require one upper case letter and one digit
        assertFalse(validator.isValid("1password", null));
        assertFalse(validator.isValid("Ppassword", null));

        // valid passwords
        assertTrue(validator.isValid("1Pass", null));
        assertTrue(validator.isValid("eqs123Pasfewr2s", null));
    }


}
