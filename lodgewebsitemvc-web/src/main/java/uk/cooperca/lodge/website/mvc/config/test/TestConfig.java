package uk.cooperca.lodge.website.mvc.config.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.cooperca.lodge.website.mvc.service.ReviewService;
import uk.cooperca.lodge.website.mvc.service.UserService;

import static org.mockito.Mockito.mock;

/**
 * Configuration class for testing our controllers. Creates mock versions of the service classes.
 *
 * @author Charlie Cooper
 */
@Configuration
public class TestConfig {

    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    @Bean
    public ReviewService reviewService() {
        return mock(ReviewService.class);
    }
}
