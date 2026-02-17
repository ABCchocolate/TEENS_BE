package kusuri12.teens_be.global.validation.validator;

import com.vane.badwordfiltering.BadWordFiltering;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kusuri12.teens_be.global.validation.anotation.Ban;

public class BanValidator implements ConstraintValidator<Ban, String> {

    BadWordFiltering badWordFiltering = new BadWordFiltering();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        return !badWordFiltering.check(value);
    }
}
