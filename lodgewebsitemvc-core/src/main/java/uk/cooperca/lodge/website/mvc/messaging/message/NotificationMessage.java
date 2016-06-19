package uk.cooperca.lodge.website.mvc.messaging.message;

/**
 * A message for the notification module.
 *
 * @author Charlie Cooper
 */
public class NotificationMessage {

    public enum Type {
        EMAIL_UPDATE
    }

    private Type type;
    private Integer userId;

    public NotificationMessage() {
    }

    public NotificationMessage(Type type, Integer userId) {
        this.type = type;
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public Integer getUserId() {
        return userId;
    }
}