package uk.cooperca.lodge.website.mvc.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.cooperca.lodge.website.mvc.entity.User;

import java.util.Optional;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends AbstractControllerTest {

    @Test
    public void testVerify() throws Exception {
        String token = "mytesttoken";

        // service returns null
        User user = mock(User.class);
        when(user.getId()).thenReturn(1);
        when(userService.verifyUser(token)).thenReturn(
                null, user
        );
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/user/verify").with(csrf()).param("token", "mytesttoken"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Unable to verify your account, maybe you are already verified?"))
                .andExpect(view().name("verificationFailure"));

        // success
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

    @Test
    public void testForgottenPassword() throws Exception {
        mockMvc.perform(get("/user/forgottenPassword").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("forgottenPassword"));
    }

    @Test
    public void testPasswordCommandValidation() throws Exception {
        String email = "";
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.getUserByEmail("test@yahoo.com")).thenReturn(Optional.of(mock(User.class)));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);

        // missing email
        expectPasswordError(map);

        // incorrect email
        map.replace("email", asList("sdfadfsd.com"));

        // non-existent user
        map.replace("email", asList("test2@yahoo.com"));

        // working
        map.replace("email", asList("test@yahoo.com"));
    }

    private void expectPasswordError(MultiValueMap<String, String> map) throws Exception {
        mockMvc.perform(post("/user/requestPassword")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(map))
                .andExpect(status().isOk())
                .andExpect(view().name("forgottenPassword"))
                .andExpect(model().attributeHasFieldErrors("passwordCommand"));
        // TODO: finish test
//        verify(userService, never()).registerUser(Matchers.any(UserCommand.class), Matchers.any(Locale.class));
    }
}
