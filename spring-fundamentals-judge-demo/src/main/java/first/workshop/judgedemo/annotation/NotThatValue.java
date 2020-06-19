package first.workshop.judgedemo.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Documented
@Constraint(validatedBy = AllowedValuesValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface NotThatValue {

    String message() default "Invalid value";

    Class<?>[] groups() default {
    };

    Class<? extends Payload>[] payload() default {};

    String forbiddenValue();
}
