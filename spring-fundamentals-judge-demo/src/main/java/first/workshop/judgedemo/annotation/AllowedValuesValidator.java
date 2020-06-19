package first.workshop.judgedemo.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AllowedValuesValidator implements ConstraintValidator<NotThatValue, String> {
    private String value;
    private String message;

    public void initialize(NotThatValue constraint) {
        this.value = constraint.forbiddenValue();
        this.message = constraint.message();
    }

    public boolean isValid(String val, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(message);

        return !val.equals(value);
    }
}
