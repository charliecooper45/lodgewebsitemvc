package uk.cooperca.lodge.website.mvc.command.constraint.validator;

import org.junit.Test;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.command.constraint.Equals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EqualsValidatorTest {

    @Test
    public void test() {
        EqualsValidator validator = new EqualsValidator();
        Equals equals = mock(Equals.class);
        when(equals.first()).thenReturn("email");
        when(equals.second()).thenReturn("confirmEmail");
        validator.initialize(equals);

        // not matching
        UserCommand command = new UserCommand();
        command.setEmail("bob@gmail.com");
        command.setConfirmEmail("bob@gmail.co.uk");
        assertFalse(validator.isValid(command, null));

        // one blank
        command = new UserCommand();
        command.setEmail("bob@gmail.com");
        command.setConfirmEmail("");
        assertFalse(validator.isValid(command, null));

        // one null
        command = new UserCommand();
        command.setEmail("bob@gmail.com");
        assertFalse(validator.isValid(command, null));

        // matching
        command = new UserCommand();
        command.setEmail("bob@gmail.com");
        command.setConfirmEmail("bob@gmail.com");
        assertTrue(validator.isValid(command, null));

        // both null
        command = new UserCommand();
        assertTrue(validator.isValid(command, null));
    }
}
