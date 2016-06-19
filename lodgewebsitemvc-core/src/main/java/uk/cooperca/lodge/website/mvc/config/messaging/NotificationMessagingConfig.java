package uk.cooperca.lodge.website.mvc.config.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static uk.cooperca.lodge.website.mvc.config.messaging.MessagingConfig.getRoutingKey;

/**
 * Configuration class that configures all Queues, Exchanges and Bindings for the notification module.
 *
 * @author Charlie Cooper
 */
@Configuration
public class NotificationMessagingConfig {

    // exchanges
    public static final String NOTIFICATION_TOPIC = "notification.topic";

    // queues
    public static final String NOTIFICATION_EMAIL_UPDATE = "notification.queue";

    @Bean
    public Exchange notificationTopic() {
        return new TopicExchange(NOTIFICATION_TOPIC, true, false);
    }

    @Bean
    public Queue notificationEmailUpdate() {
        return new Queue(NOTIFICATION_EMAIL_UPDATE);
    }

    @Bean
    public Binding notificationEmailUpdateTopicBinding() {
        // routing key = notification.queue (the same as the queue name for now)
        return BindingBuilder.bind(notificationEmailUpdate()).to(notificationTopic()).with(getRoutingKey(NOTIFICATION_EMAIL_UPDATE)).noargs();
    }
}
