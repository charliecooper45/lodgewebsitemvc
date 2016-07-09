package uk.cooperca.lodge.website.mvc.command.constraint;

import uk.cooperca.lodge.website.mvc.command.constraint.validator.NoUserWithEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation constraint that checks if no user with the given email exists;
 *
 * @author Charlie Cooper
 */
@Documented
@Constraint(validatedBy = NoUserWithEmailValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface NoUserWithEmail {

    String message() default "{constraints.NoUserWithEmail.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
