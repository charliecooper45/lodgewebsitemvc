package uk.cooperca.lodge.website.mvc.password.impl;

import org.apache.commons.lang.RandomStringUtils;
import uk.cooperca.lodge.website.mvc.password.PasswordGenerator;

/**
 * Simple {@link PasswordGenerator} implementation that generates a random string.
 *
 * @author Charlie Cooper
 */
public class RandomPasswordGenerator implements PasswordGenerator {

    @Override
    public String generatePassword() {
        return RandomStringUtils.random(15, true, true);
    }
}
