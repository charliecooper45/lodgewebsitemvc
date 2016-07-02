package uk.cooperca.lodge.website.mvc.config.test;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.cooperca.lodge.website.mvc.messaging.NotificationMessageProducer;
import uk.cooperca.lodge.website.mvc.repository.ReviewRepository;
import uk.cooperca.lodge.website.mvc.repository.RoleRepository;
import uk.cooperca.lodge.website.mvc.repository.UserRepository;
import uk.cooperca.lodge.website.mvc.token.TokenManager;

import static org.mockito.Mockito.mock;

/**
 * Configuration class for testing services.
 *
 * @author Charlie Cooper
 */
@Configuration
@ComponentScan(basePackages = { "uk.cooperca.lodge.website.mvc.service" })
public class TestConfig {

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public RoleRepository roleRepository() {
        return mock(RoleRepository.class);
    }

    @Bean
    public ReviewRepository reviewRepository() {
        return mock(ReviewRepository.class);
    }

    @Bean
    public NotificationMessageProducer notificationMessageProducer() {
        return mock(NotificationMessageProducer.class);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return mock(RabbitTemplate.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TokenManager tokenManager() {
        return new TokenManager("mysecret", "http://localhost:8080");
    }
}
