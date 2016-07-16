package uk.cooperca.lodge.website.mvc.config.test;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import uk.cooperca.lodge.website.mvc.config.messaging.MessagingConfig;
import uk.cooperca.lodge.website.mvc.consumer.NotificationMessageConsumers;
import uk.cooperca.lodge.website.mvc.service.impl.EmailNotificationService;
import uk.cooperca.lodge.website.mvc.link.LinkBuilder;
import uk.cooperca.lodge.website.mvc.token.TokenManager;
import uk.cooperca.lodge.website.mvc.service.UserService;

import static org.mockito.Mockito.mock;

/**
 * Configuration class for testing messaging.
 *
 * @author Charlie Cooper
 */
@Configuration
@RabbitListenerTest
@Import(value = {MessagingConfig.class})
public class TestConfig {

    @Bean
    public NotificationMessageConsumers consumers() {
        return new NotificationMessageConsumers();
    }

    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    @Bean
    public EmailNotificationService emailService() {
        return mock(EmailNotificationService.class);
    }

    @Bean
    public JavaMailSender mailSender() {
        return mock(JavaMailSender.class);
    }

    @Bean
    public VelocityEngine velocityEngine() {
        return mock(VelocityEngine.class);
    }

    @Bean
    public LinkBuilder linkBuilder() {
        return mock(LinkBuilder.class);
    }

    @Bean
    public TokenManager tokenManager() {
        return mock(TokenManager.class);
    }
}
