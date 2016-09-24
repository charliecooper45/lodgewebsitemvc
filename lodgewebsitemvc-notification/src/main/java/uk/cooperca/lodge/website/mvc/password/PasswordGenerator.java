package uk.cooperca.lodge.website.mvc.password;

/**
 * Interface that marks a class possible of generating new user passwords.
 *
 * @author Charlie Cooper
 */
public interface PasswordGenerator {

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public String generatePassword();
}
