package uk.cooperca.lodge.website.mvc.command.constraint.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.cooperca.lodge.website.mvc.command.constraint.Equals;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * {@link ConstraintValidator} implementation for the {@link Equals} annotation.
 *
 * @author Charlie Cooper
 */
public class EqualsValidator implements ConstraintValidator<Equals, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EqualsValidator.class);

    private String first;
    private String second;

    @Override
    public void initialize(Equals equals) {
        first = equals.first();
        second = equals.second();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            final Object firstValue = BeanUtils.getProperty(object, first);
            final Object secondValue = BeanUtils.getProperty(object, second);

            if (firstValue == null && secondValue == null) {
                return true;
            }
            if (firstValue != null && firstValue.equals(secondValue)) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.warn("Error comparing property {}", object, e);
        }
        return false;
    }
}
