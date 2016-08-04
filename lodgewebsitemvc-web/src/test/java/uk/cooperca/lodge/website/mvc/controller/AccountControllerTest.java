package uk.cooperca.lodge.website.mvc.controller;

import org.junit.Test;
import org.springframework.security.core.context.SecurityContext;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.test.WithCustomUser;

import javax.servlet.http.HttpSession;
import java.io.UncheckedIOException;
import java.net.ConnectException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountControllerTest extends AbstractControllerTest {

    @Test
    public void testAccount() throws Exception {
        mockMvc.perform(get("/account").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithCustomUser
    public void testAccountWithUser() throws Exception {
        mockMvc.perform(get("/account").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", any(User.class)))
                .andExpect(view().name("account"));
    }

    @Test
    public void testUpdateNotLoggedIn() throws Exception {
        String newEmail = "bill@hotmail.com";
        UserCommand command = new UserCommand();
        command.setConfirmEmail(newEmail);
        mockMvc.perform(put("/account/email").with(csrf()).contentType(APPLICATION_JSON).content(getObjectWriter().writeValueAsString(command)))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
        verifyZeroInteractions(userService);
    }

    @Test
    @WithCustomUser
    public void testUpdateEmail() throws Exception {
        String newEmail = "bill@hotmail.com";
        User currentUser = getCurrentUser();
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        Optional<User> optional = Optional.of(new User(newEmail, currentUser.getPassword(), currentUser.getFirstName(), currentUser.getLastName(),
                currentUser.getRole(), currentUser.getLanguage(), currentUser.isVerified(), currentUser.getCreatedAt()));
        when(userService.getUserByEmail(newEmail)).thenReturn(
                optional,
                Optional.empty(),
                Optional.empty(),
                optional
        );

        // existing email
        UserCommand command = new UserCommand();
        command.setEmail(newEmail);
        command.setConfirmEmail(newEmail);
        String data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/email").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Email already in use")));
        verify(userService, never()).updateEmail(anyString(), anyInt());

        // blank emails
        command = new UserCommand();
        command.setEmail("");
        command.setConfirmEmail("");
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/email").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$.[*]", containsInAnyOrder(
                        "Email is required",
                        "Not a valid email address"
                )));
        verify(userService, never()).updateEmail(anyString(), anyInt());

        // not a valid email
        command = new UserCommand();
        command.setEmail("asdsad");
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/email").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$.[*]", containsInAnyOrder(
                    "Both email fields must match",
                    "Not a valid email address"
                )));
        verify(userService, never()).updateEmail(anyString(), anyInt());

        // email addresses don't match
        command.setEmail(newEmail);
        command.setConfirmEmail("bill@hotmail2.com");
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/email").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Both email fields must match")));
        verify(userService, never()).updateEmail(anyString(), anyInt());

        // correct update
        command.setConfirmEmail(newEmail);
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/email").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Email has been updated")))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    User user = (User) securityContext.getAuthentication().getPrincipal();
                    assertEquals(user.getEmail(), newEmail);
                });
        verify(userService).updateEmail(eq(newEmail), anyInt());
    }

    @Test
    @WithCustomUser
    public void testUpdatePassword() throws Exception {
        String newPassword = "A1ValidPass";
        User currentUser = getCurrentUser();
        when(userService.getUserByEmail(currentUser.getEmail())).thenReturn(
                Optional.of(new User(currentUser.getEmail(), newPassword, currentUser.getFirstName(), currentUser.getLastName(),
                        currentUser.getRole(), currentUser.getLanguage(), currentUser.isVerified(), currentUser.getCreatedAt()))
        );

        // not a valid password
        UserCommand command = new UserCommand();
        command.setPassword("notavalidpassword");
        command.setConfirmPassword("notavalidpassword");
        String data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/password").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Password must be between 5 and 15 characters long and contain one upper case character and a digit")));
        verify(userService, never()).updatePassword(anyString(), anyInt());

        // passwords don't match
        command.setPassword(newPassword);
        command.setConfirmPassword("notavalidpassword");
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/password").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Both password fields must match")));
        verify(userService, never()).updatePassword(anyString(), anyInt());

        // correct update
        command.setPassword(newPassword);
        command.setConfirmPassword(newPassword);
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/password").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Password has been updated")))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    User user = (User) securityContext.getAuthentication().getPrincipal();
                    assertEquals(user.getPassword(), "A1ValidPass");
                });
        verify(userService).updatePassword(eq(newPassword), anyInt());
    }

    @Test
    @WithCustomUser
    public void testUpdateFirstName() throws Exception {
        String newFirstName = "William";
        User currentUser = getCurrentUser();
        when(userService.getUserByEmail(currentUser.getEmail())).thenReturn(
                Optional.of(new User(currentUser.getEmail(), currentUser.getPassword(), newFirstName, currentUser.getLastName(),
                        currentUser.getRole(), currentUser.getLanguage(), currentUser.isVerified(), currentUser.getCreatedAt()))
        );

        // blank first name
        UserCommand command = new UserCommand();
        String data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/firstname").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("First name is required")));
        verifyZeroInteractions(userService);

        // correct update
        command.setFirstName(newFirstName);
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/firstname").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("First name has been updated")))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    User user = (User) securityContext.getAuthentication().getPrincipal();
                    assertEquals(user.getFirstName(), newFirstName);
                });
        verify(userService).getUserByEmail(currentUser.getEmail());
        verify(userService).updateFirstName(eq(newFirstName), anyInt());
    }

    @Test
    @WithCustomUser
    public void testUpdateLastName() throws Exception {
        String newLastName = "Smith";
        User currentUser = getCurrentUser();
        when(userService.getUserByEmail(currentUser.getEmail())).thenReturn(
                Optional.of(new User(currentUser.getEmail(), currentUser.getPassword(), currentUser.getFirstName(), newLastName,
                        currentUser.getRole(), currentUser.getLanguage(), currentUser.isVerified(), currentUser.getCreatedAt()))
        );

        // blank first name
        UserCommand command = new UserCommand();
        String data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/lastname").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Last name is required")));
        verifyZeroInteractions(userService);

        // correct update
        command.setLastName(newLastName);
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/lastname").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Last name has been updated")))
                .andExpect(mvcResult -> {
                    HttpSession session = mvcResult.getRequest().getSession();
                    SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    User user = (User) securityContext.getAuthentication().getPrincipal();
                    assertEquals(user.getLastName(), newLastName);
                });
        verify(userService).getUserByEmail(currentUser.getEmail());
        verify(userService).updateLastName(eq(newLastName), anyInt());
    }

    @Test
    @WithCustomUser
    public void testUpdateWithException() throws Exception {
        String newLastName = "Smith";
        when(userService.updateLastName("Smith", 0)).thenThrow(new UncheckedIOException(new ConnectException()));

        UserCommand command = new UserCommand();
        command.setLastName(newLastName);
        String data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(put("/account/lastname").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Unable to update field")));
    }

    @Test
    @WithCustomUser
    public void testUpdateLanguage() throws Exception {
        when(userService.updateLanguage("ef", 0)).thenThrow(new IllegalArgumentException());
        User user = mock(User.class);
        when(userService.getUserByEmail("bill@gmail.com")).thenReturn(Optional.of(user));
        when(user.getLanguage()).thenReturn(User.Language.EN);

        // incorrect language
        mockMvc.perform(put("/account/language?language=ef").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Language with code ef is not supported")));

        // success
        mockMvc.perform(put("/account/language?language=en").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Language has been updated")));
    }
}
