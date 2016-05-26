package uk.cooperca.lodge.website.mvc.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.cooperca.lodge.website.mvc.config.WebMvcConfig;

/**
 * Abstract base class for all controller test classes.
 *
 * @author Charlie Cooper
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class})
@WebAppConfiguration
@TestPropertySource(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=password"})
public abstract class AbstractControllerTest {

    @Autowired
    protected WebApplicationContext applicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        // TODO: reset mocks
        //Mockito.reset(todoServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }
}
