package uk.cooperca.lodge.website.mvc.messaging;

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

    @Autowired
    private RabbitTemplate template;

    public void sendMessage(NotificationMessage.Type type, int id) {
        template.convertAndSend(new NotificationMessage(type, id));
    }
}
