package uk.cooperca.lodge.website.mvc.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.Test;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends AbstractControllerTest {

    @Test
    public void testVerify() throws Exception {
        String token = "mytesttoken";

        // service returns false
        mockMvc.perform(get("/user/verify").with(csrf()).param("token", "mytesttoken"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Unable to verify your account, maybe you are already verified?"))
                .andExpect(view().name("verificationFailure"));

        // success
        when(userService.verifyUser(token)).thenReturn(
                true
        );
        mockMvc.perform(get("/user/verify").with(csrf()).param("token", "mytesttoken"))
                .andExpect(status().isFound())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(redirectedUrl("/user/verificationSuccess"));

        // exception handling
        when(userService.verifyUser(token)).thenThrow(
                new MalformedJwtException(""),
                new SignatureException(""),
                new ExpiredJwtException(null, null, "")
        );
        mockMvc.perform(get("/user/verify").with(csrf()).param("token", "mytesttoken"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Invalid verification link"))
                .andExpect(view().name("verificationFailure"));
        mockMvc.perform(get("/user/verify").with(csrf()).param("token", "mytesttoken"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Invalid verification link"))
                .andExpect(view().name("verificationFailure"));
        mockMvc.perform(get("/user/verify").with(csrf()).param("token", "mytesttoken"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Verification link has expired"))
                .andExpect(view().name("verificationFailure"));
    }

    @Test
    public void testVerificationSuccess() throws Exception {
        mockMvc.perform(get("/user/verificationSuccess").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("verificationSuccess"));
    }
}
