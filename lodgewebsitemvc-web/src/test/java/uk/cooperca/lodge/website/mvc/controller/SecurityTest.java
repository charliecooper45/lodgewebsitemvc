package uk.cooperca.lodge.website.mvc.controller;

import org.junit.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.cooperca.lodge.website.mvc.config.test.TestConfig.USERNAME;
import static uk.cooperca.lodge.website.mvc.config.test.TestConfig.PASSWORD;

public class SecurityTest extends AbstractControllerTest {

    @Test
    public void testAccount() throws Exception {
        mockMvc.perform(get("/account").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(USERNAME)
    public void testAccountWithUser() throws Exception {
        mockMvc.perform(get("/account").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));
    }

    @Test
    public void testCorrectLogin() throws Exception {
        mockMvc.perform(post("/login").with(csrf()).param("username", USERNAME).param("password", PASSWORD))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("account"))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    assertEquals(securityContext.getAuthentication().getName(), USERNAME);
                });
    }

    @Test
    public void testFailedLogin() throws Exception {
        mockMvc.perform(post("/login").with(csrf()).param("username", "fred@hotmail.com").param("password", PASSWORD))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login-error"))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    assertNull(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY));
                });

        mockMvc.perform(post("/login").with(csrf()).param("username", USERNAME).param("password", "letmein"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login-error"))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    assertNull(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY));
                });
    }
}
