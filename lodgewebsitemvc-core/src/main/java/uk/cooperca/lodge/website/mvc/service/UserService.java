package uk.cooperca.lodge.website.mvc.service;

import org.joda.time.DateTime;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.entity.User;

import java.util.Locale;
import java.util.Optional;

/**
 * Service that allows the management of {@link User}s.
 *
 * @author Charlie Cooper
 */
public interface UserService {

    /**
     * Retrieves the user with the given identifier.
     *
     * @param id the identifier of the user
     *
     * @return an optional containing the user if present
     */
    public Optional<User> getUserById(int id);

    /**
     * Retrieves the user with the given email address.
     *
     * @param email the email address of the user
     *
     * @return an optional containing the user if present
     */
    public Optional<User> getUserByEmail(String email);

    /**
     * Retrieves all unverified users that have not had a verification request sent since the given date.
     *
     * @param before the date verification was last requested
     *
     * @return the unverified users
     */
    public Iterable<User> getUnverifiedUsersBefore(DateTime before);

    /**
     * Registers a new user on the lodge website platform.
     *
     * @param command the command object holding the registration request
     * @param locale the locale of the new user
     *
     * @return the registered user
     */
    public User registerUser(UserCommand command, Locale locale);

    /**
     * Requests a verification email for the user with the given id.
     *
     * @param id of the user to request the email for
     */
    public void requestVerificationEmail(int id);

    /**
     * Verifies a users using the given JWT token.
     *
     * @param token the JWT token
     *
     * @return the verified user if successful or null
     */
    public User verifyUser(String token);

    /**
     * Sends a reminder to the user to verify their account.
     *
     * @param id of the user to remind
     */
    public void sendVerificationReminder(int id);

    /**
     * Sends a new password to the user.
     *
     * @param id of the user to send the password to
     */
    public void sendPasswordResetRequest(int id);

    /**
     * Updates the given user's email address.
     *
     * @param email the new email address for the user
     * @param id the id of the user to update
     */
    public void updateEmail(String email, int id);

    /**
     * Updates the given user's password.
     *
     * @param password the new password for the user
     * @param id the id of the user to update
     */
    public void updatePassword(String password, int id);

    /**
     * Updates the given user's first name.
     *
     * @param firstName the new first name for the user
     * @param id the id of the user to update
     */
    public void updateFirstName(String firstName, int id);

    /**
     * Updates the given user's last name.
     *
     * @param lastName the new last name for the user
     * @param id the id of the user to update
     */
    public void updateLastName(String lastName, int id);

    /**
     * Updates the given user's language preference.
     *
     * @param language the new language for the user
     * @param id the id of the user to update
     */
    public void updateLanguage(String language, int id);
}
