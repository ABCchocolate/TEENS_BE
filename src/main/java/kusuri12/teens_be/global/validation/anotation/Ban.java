package kusuri12.teens_be.global.validation.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kusuri12.teens_be.global.validation.validator.BanValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BanValidator.class)
public @interface Ban {
    String message() default "{Ban}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
