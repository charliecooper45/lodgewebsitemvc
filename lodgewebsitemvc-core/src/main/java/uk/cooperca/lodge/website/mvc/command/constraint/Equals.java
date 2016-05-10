package uk.cooperca.lodge.website.mvc.command.constraint;

import uk.cooperca.lodge.website.mvc.command.constraint.validator.EqualsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation constraint that checks if two fields are equal.
 *
 * Greatly inspired by
 * <a href="http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303">this</a>
 * Stack Overflow post.
 *
 * @author Charlie Cooper
 */
@Documented
@Constraint(validatedBy = EqualsValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(Equals.List.class)
public @interface Equals {

    String message() default "{constraints.Equals.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

    String first();

    String second();

    @Documented
    @Target({TYPE})
    @Retention(RUNTIME)
    @interface List {
        Equals[] value();
    }
}
