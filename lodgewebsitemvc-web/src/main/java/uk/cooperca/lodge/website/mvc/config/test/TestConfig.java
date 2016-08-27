package uk.cooperca.lodge.website.mvc.config.test;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import uk.cooperca.lodge.website.mvc.token.TokenManager;
import uk.cooperca.lodge.website.mvc.service.ReviewService;
import uk.cooperca.lodge.website.mvc.service.UserService;
import uk.cooperca.lodge.website.mvc.service.impl.UserDetailsServiceImpl;

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

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TokenManager tokenManager() {
        return mock(TokenManager.class);
    }

    @Bean
    public SecurityExpressionHandler securityExpressionHandler() {
        return new DefaultWebSecurityExpressionHandler();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
