package uk.cooperca.lodge.website.mvc.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.config.CoreConfig;
import uk.cooperca.lodge.website.mvc.config.SecurityConfig;
import uk.cooperca.lodge.website.mvc.messaging.NotificationMessageProducer;
import uk.cooperca.lodge.website.mvc.repository.UserRepository;
import uk.cooperca.lodge.website.mvc.security.token.TokenManager;

/**
 * Abstract base class that loads the {@link CoreConfig} configuration class in the test Spring profile. This allows us
 * to mock objects for testing services.
 *
 * @author Charlie Cooper
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, SecurityConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=password"})
public abstract class AbstractServiceTest {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected NotificationMessageProducer producer;

    @Autowired
    protected TokenManager tokenManager;
}
