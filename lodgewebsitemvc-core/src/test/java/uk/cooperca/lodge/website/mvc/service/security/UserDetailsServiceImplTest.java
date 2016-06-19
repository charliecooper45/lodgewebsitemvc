package uk.cooperca.lodge.website.mvc.service.security;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.cooperca.lodge.website.mvc.AbstractCoreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class UserDetailsServiceImplTest extends AbstractCoreTest {

    @Autowired
    private UserDetailsService service;

    @Test
    public void test() {
        assertNotNull(service);

        UserDetails userDetails = service.loadUserByUsername("testcc45@gmail.com");
        assertNotNull(userDetails);
        assertEquals("testcc45@gmail.com", userDetails.getUsername());

        try {
            service.loadUserByUsername("testcc45@hotmail.com");
            fail();
        } catch (UsernameNotFoundException e) {
            assertEquals("email address not registered", e.getMessage());
        }
    }
}
