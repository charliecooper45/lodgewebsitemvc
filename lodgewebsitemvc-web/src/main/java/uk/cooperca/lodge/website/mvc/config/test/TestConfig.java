package uk.cooperca.lodge.website.mvc.config.test;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import uk.cooperca.lodge.website.mvc.entity.Role;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.ReviewService;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Configuration class for testing our controllers. Creates mock versions of the service classes.
 *
 * @author Charlie Cooper
 */
@Configuration
public class TestConfig {

    public static final String USERNAME = "bill@gmail.com";
    public static final String PASSWORD = "23asds232dwaDsa";

    @Bean
    public UserService userService() {
        UserService userService = mock(UserService.class);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        addTestUsers(userService);
        return userService;
    }

    private void addTestUsers(UserService userService) {
        Role userRole = new Role(Role.RoleName.ROLE_USER, DateTime.now());
        User user = new User(USERNAME, PASSWORD, "Sandra", "Smith", userRole, User.Language.EN,
                DateTime.now());
        when(userService.getUserByEmail(USERNAME)).thenReturn(Optional.of(user));
    }

    @Bean
    public ReviewService reviewService() {
        return mock(ReviewService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityExpressionHandler securityExpressionHandler() {
        return new DefaultWebSecurityExpressionHandler();
    }
}
