package uk.cooperca.lodge.website.mvc.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage;

/**
 * Sends messages to the notification backend module.
 *
 * @author Charlie Cooper
 */
@Component
public class NotificationMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageProducer.class);

    @Autowired
    private RabbitTemplate template;

    /**
     * Sends a message of the given type to the notification backend throwing an exception if an error occurs.
     *
     * @param type the message type
     * @param id the id of the entity related to this message
     */
    public void sendMessage(NotificationMessage.Type type, int id) {
        template.convertAndSend(new NotificationMessage(type, id));
    }

    /**
     * Sends a message of the given type to the notification backend and handles any errors that occur silently.
     *
     * @param type the message type
     * @param id the id of the entity related to this message
     */
    public void sendMessageHandleError(NotificationMessage.Type type, int id) {
        try {
            template.convertAndSend(new NotificationMessage(type, id));
        } catch (Exception e) {
            LOGGER.warn("Unable to send message of type {} with entity ID {}", type, id, e);
        }
    }
}
