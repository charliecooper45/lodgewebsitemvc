package uk.cooperca.lodge.website.mvc.controller;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContext;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.entity.User.Language;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SecurityTest extends AbstractControllerTest {

    private String username = "ed@hotmail.com";
    private String password = "1ferraRi";

    @Before
    public void setup() {
        super.setup();
        when(userService.getUserByEmail(username)).thenReturn(
                Optional.of(new User(username, password, "Edward", "Phillips", getUserRole(), Language.EN, DateTime.now()))
        );
    }

    @Test
    public void testCorrectLogin() throws Exception {
        mockMvc.perform(post("/login").with(csrf()).param("username", username).param("password", password))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("account"))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    assertEquals(securityContext.getAuthentication().getName(), username);
                });
    }

    @Test
    public void testFailedLogin() throws Exception {
        String username2 = "fred@hotmail.com";
        when(userService.getUserByEmail(username2)).thenReturn(Optional.empty());
        mockMvc.perform(post("/login").with(csrf()).param("username", username2).param("password", password))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login-error"))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    assertNull(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY));
                });

        mockMvc.perform(post("/login").with(csrf()).param("username", username).param("password", "letmein"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login-error"))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    assertNull(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY));
                });
    }
}
