package uk.cooperca.lodge.website.mvc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage;
import uk.cooperca.lodge.website.mvc.email.EmailService;

import static uk.cooperca.lodge.website.mvc.config.messaging.NotificationMessagingConfig.NOTIFICATION_EMAIL_UPDATE;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.EMAIL_UPDATE;

/**
 * Defines all the message consumers that consume {@link NotificationMessage}s.
 *
 * @author Charlie Cooper
 */
@Component
public class NotificationMessageConsumers {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageConsumers.class);

    @Autowired
    private EmailService notifier;

    @RabbitListener(id = NOTIFICATION_EMAIL_UPDATE, queues = NOTIFICATION_EMAIL_UPDATE)
    public void receiveMessage(NotificationMessage message) {
        LOGGER.info("Received message of type {} for user with ID {}", message.getType(), message.getUserId());
        notifier.sendEmail(EMAIL_UPDATE, message.getUserId());
    }
}
