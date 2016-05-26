package uk.cooperca.lodge.website.mvc.controller;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.cooperca.lodge.website.mvc.command.RegisterCommand;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegisterControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;
    
    private final String email = "sarah@gmail.com";
    private final String password = "1Password";
    private final String firstName = "Sarah";
    private final String lastName = "Smith";

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("registerCommand", any(RegisterCommand.class)))
            .andExpect(view().name("register"));
    }

    @Test
    public void testDoRegister() throws Exception {
        when(userService.getUserByEmail(email)).thenReturn(
                Optional.of(mock(User.class)),
                Optional.empty()
        );
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("asds23213ads23rrw3qdwqd");
        when(user.getAuthorities()).thenReturn(Collections.emptyList());
        when(userService.registerUser(anyObject(), anyObject())).thenReturn(
                user
        );
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("confirmEmail", email);
        map.add("password", password);
        map.add("confirmPassword", password);
        map.add("firstName", firstName);
        map.add("lastName", lastName);

        // existing user
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(map))
                .andExpect(status().isOk())
                .andExpect(view().name("registerFailure"))
                .andExpect(model().attribute("errorText", "Email address sarah@gmail.com is already in use."));
        verify(userService).getUserByEmail(email);
        verifyZeroInteractions(userService);

        // new user
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(map))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/register/success"))
                .andExpect(redirectedUrl("/register/success"))
                .andExpect(model().hasNoErrors());
        verify(userService, times(2)).getUserByEmail(email);
        ArgumentCaptor<RegisterCommand> argumentCaptor = ArgumentCaptor.forClass(RegisterCommand.class);
        verify(userService).registerUser(argumentCaptor.capture(), anyObject());
        verifyNoMoreInteractions(userService);

        RegisterCommand command = argumentCaptor.getValue();
        assertEquals(email, command.getEmail());
        assertEquals(email, command.getConfirmEmail());
        assertEquals(password, command.getPassword());
        assertEquals(password, command.getConfirmPassword());
        assertEquals(firstName, command.getFirstName());
        assertEquals(lastName, command.getLastName());
    }

    @Test
    public void testValidation() throws Exception {
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
    }

    private void expectFieldError(MultiValueMap<String, String> map, String field) throws Exception {
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(map))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("registerCommand", field));
        verifyZeroInteractions(userService);
    }
}
