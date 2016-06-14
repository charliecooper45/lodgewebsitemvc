package uk.cooperca.lodge.website.mvc.command;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import uk.cooperca.lodge.website.mvc.command.constraint.Equals;
import uk.cooperca.lodge.website.mvc.command.constraint.Password;
import uk.cooperca.lodge.website.mvc.command.constraint.group.UserValidationGroups.EmailValidationGroup;
import uk.cooperca.lodge.website.mvc.command.constraint.group.UserValidationGroups.FirstNameValidationGroup;
import uk.cooperca.lodge.website.mvc.command.constraint.group.UserValidationGroups.LastNameValidationGroup;

import javax.validation.groups.Default;

/**
 * Command object that represents an attempt to update/create a user on the platform.
 *
 * @author Charlie Cooper
 */
@Equals(groups = {Default.class, EmailValidationGroup.class}, first = "email", second = "confirmEmail")
@Equals(first = "password", second = "confirmPassword")
public class UserCommand {

    @Email(groups = {Default.class, EmailValidationGroup.class},
           regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    @NotBlank(groups = {Default.class, EmailValidationGroup.class})
    private String email;

    private String confirmEmail;

    @Password(min = 5, max = 15)
    private String password;

    private String confirmPassword;

    @NotBlank(groups = {Default.class, FirstNameValidationGroup.class})
    private String firstName;

    @NotBlank(groups = {Default.class, LastNameValidationGroup.class})
    private String lastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
