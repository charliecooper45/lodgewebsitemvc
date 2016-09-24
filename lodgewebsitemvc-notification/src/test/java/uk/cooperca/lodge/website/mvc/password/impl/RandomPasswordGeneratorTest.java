package uk.cooperca.lodge.website.mvc.password.impl;

import org.junit.Test;
import uk.cooperca.lodge.website.mvc.password.PasswordGenerator;

import static org.junit.Assert.assertNotEquals;

public class RandomPasswordGeneratorTest {

    @Test
    public void test() {
        PasswordGenerator passwordGenerator = new RandomPasswordGenerator();

        String password1 = passwordGenerator.generatePassword();
        String password2 = passwordGenerator.generatePassword();

        assertNotEquals(password1, password2);
    }
}
