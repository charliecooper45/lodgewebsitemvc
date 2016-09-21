package uk.cooperca.lodge.website.mvc.command;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import uk.cooperca.lodge.website.mvc.command.constraint.UserWithEmail;

/**
 * Command object that represents a request for a new password.
 *
 * @author Charlie Cooper
 */
public class PasswordCommand {

    @Email(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    @NotBlank
    @UserWithEmail
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
