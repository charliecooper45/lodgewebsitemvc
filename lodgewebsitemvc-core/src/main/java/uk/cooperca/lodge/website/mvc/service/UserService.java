package uk.cooperca.lodge.website.mvc.service;

import uk.cooperca.lodge.website.mvc.command.RegisterCommand;
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
     * Retrieves the user with the given email address.
     *
     * @param email the email address of the user
     *
     * @return an optional containing the user if present
     */
    public Optional<User> getUserByEmail(String email);

    /**
     * Registers a new user on the lodge website platform.
     *
     * @param command the command object holding the registration request
     * @param locale the locale of the new user
     *
     * @return the registered user
     */
    public User registerUser(RegisterCommand command, Locale locale);
}
