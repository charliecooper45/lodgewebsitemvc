package uk.cooperca.lodge.website.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.cooperca.lodge.website.mvc.config.SecurityConfig;
import uk.cooperca.lodge.website.mvc.config.WebMvcConfig;
import uk.cooperca.lodge.website.mvc.entity.Role;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.nio.charset.Charset;

import static org.mockito.Mockito.reset;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Abstract base class for all controller test classes.
 *
 * @author Charlie Cooper
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, SecurityConfig.class})
@WebAppConfiguration
@TestPropertySource(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=password"})
public abstract class AbstractControllerTest extends AbstractController {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private final Role userRole;
    private final ObjectWriter objectWriter;

    @Autowired
    protected WebApplicationContext applicationContext;

    @Autowired
    protected UserService userService;

    protected MockMvc mockMvc;

    public AbstractControllerTest() {
        userRole = new Role(Role.RoleName.ROLE_USER, DateTime.now());
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        reset(userService);
    }

    protected ObjectWriter getObjectWriter() {
        return objectWriter;
    }

    protected Role getUserRole() {
        return userRole;
    }

    protected User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
