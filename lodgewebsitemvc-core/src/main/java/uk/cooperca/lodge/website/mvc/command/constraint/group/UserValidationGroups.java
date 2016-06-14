package uk.cooperca.lodge.website.mvc.command.constraint.group;

import uk.cooperca.lodge.website.mvc.entity.User;

/**
 * Class that holds all marker interfaces for validation groups on {@link User}s.
 *
 * @author Charlie Cooper
 */
public class UserValidationGroups {

    public interface EmailValidationGroup {}

    public interface FirstNameValidationGroup {}

    public interface LastNameValidationGroup {}
}
