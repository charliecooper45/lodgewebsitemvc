package uk.cooperca.lodge.website.mvc.service;

/**
 * An interface for services that respond to notification events.
 *
 * @author Charlie Cooper
 */
public interface NotificationService {

    /**
     * Handles the event where a new user signs up to the website.
     *
     * @param userId the new user's ID
     */
    public void handleNewUserEvent(int userId);

    /**
     * Handles the event where a user updates their email address.
     *
     * @param userId the user's Id
     */
    public void handleEmailUpdateEvent(int userId);

    /**
     * Handles the event where a user updates their password.
     *
     * @param userId the user's Id
     */
    public void handlePasswordUpdateEvent(int userId);
}