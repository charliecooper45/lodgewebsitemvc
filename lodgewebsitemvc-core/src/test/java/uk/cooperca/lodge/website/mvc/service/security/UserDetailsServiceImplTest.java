package uk.cooperca.lodge.website.mvc.service.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.config.CoreConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=dev", "jasypt.encryptor.password=password"})
public class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsService service;

    @Test
    public void test() {
        assertNotNull(service);

        UserDetails userDetails = service.loadUserByUsername("bob@gmail.com");
        assertNotNull(userDetails);
        assertEquals("bob@gmail.com", userDetails.getUsername());

        try {
            service.loadUserByUsername("bob@hotmail.com");
            fail();
        } catch (UsernameNotFoundException e) {
            assertEquals("email address not registered", e.getMessage());
        }
    }
}
