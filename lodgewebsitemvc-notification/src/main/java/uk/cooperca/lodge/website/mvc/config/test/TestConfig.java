package uk.cooperca.lodge.website.mvc.config.test;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import uk.cooperca.lodge.website.mvc.config.messaging.MessagingConfig;
import uk.cooperca.lodge.website.mvc.consumer.NotificationMessageConsumers;
import uk.cooperca.lodge.website.mvc.email.EmailService;
import uk.cooperca.lodge.website.mvc.service.UserService;

import static org.mockito.Mockito.mock;

/**
 * Configuration class for testing messaging.
 *
 * @author Charlie Cooper
 */
@RabbitListenerTest
@Import(value = {MessagingConfig.class})
@Configuration
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
    public EmailService emailService() {
        return mock(EmailService.class);
    }

    @Bean
    public JavaMailSender mailSender() {
        return mock(JavaMailSender.class);
    }

    @Bean
    public VelocityEngine velocityEngine() {
        return mock(VelocityEngine.class);
    }
}
