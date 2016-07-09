package uk.cooperca.lodge.website.mvc.controller;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.entity.User;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegisterControllerTest extends AbstractControllerTest {

    private final String email = "sarah@gmail.com";
    private final String password = "1Password";
    private final String firstName = "Sarah";
    private final String lastName = "Smith";

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("userCommand", any(UserCommand.class)))
            .andExpect(view().name("register"));
    }

    @Test
    public void testDoRegister() throws Exception {
        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("asds23213ads23rrw3qdwqd");
        when(user.getAuthorities()).thenReturn(Collections.emptyList());
        when(userService.registerUser(anyObject(), anyObject())).thenReturn(user);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("confirmEmail", email);
        map.add("password", password);
        map.add("confirmPassword", password);
        map.add("firstName", firstName);
        map.add("lastName", lastName);

        // new user
        mockMvc.perform(post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(map))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/register/success"))
                .andExpect(redirectedUrl("/register/success"))
                .andExpect(model().hasNoErrors());
        verify(userService, times(1)).getUserByEmail(email);
        ArgumentCaptor<UserCommand> argumentCaptor = ArgumentCaptor.forClass(UserCommand.class);
        verify(userService).registerUser(argumentCaptor.capture(), anyObject());
        verifyNoMoreInteractions(userService);

        UserCommand command = argumentCaptor.getValue();
        assertEquals(email, command.getEmail());
        assertEquals(email, command.getConfirmEmail());
        assertEquals(password, command.getPassword());
        assertEquals(password, command.getConfirmPassword());
        assertEquals(firstName, command.getFirstName());
        assertEquals(lastName, command.getLastName());
    }

    @Test
    public void testValidation() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.getUserByEmail("test@yahoo.com")).thenReturn(Optional.of(mock(User.class)));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("confirmEmail", email);
        map.add("password", password);
        map.add("confirmPassword", password);
        map.add("firstName", firstName);
        map.add("lastName", lastName);

        // incorrect/missing email
        map.replace("email", asList("sdsad"));
        expectFieldError(map, "email");
        map.replace("email", asList(""));
        expectFieldError(map, "email");

        // missing first name
        map.replace("email", asList(email));
        map.replace("firstName", asList(""));
        expectFieldError(map, "firstName");

        // missing last name
        map.replace("firstName", asList(firstName));
        map.replace("lastName", asList(""));
        expectFieldError(map, "lastName");

        // email in use
        map.replace("email", asList("test@yahoo.com"));
        map.replace("lastName", asList(lastName));
        expectFieldError(map, "email");
    }

    private void expectFieldError(MultiValueMap<String, String> map, String field) throws Exception {
        mockMvc.perform(post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(map))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("userCommand", field));
        verify(userService, never()).registerUser(Matchers.any(UserCommand.class), Matchers.any(Locale.class));
    }
}
