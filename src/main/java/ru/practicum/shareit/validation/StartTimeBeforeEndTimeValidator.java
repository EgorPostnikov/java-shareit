package ru.practicum.shareit.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class StartTimeBeforeEndTimeValidator implements ConstraintValidator<StartTimeBeforeEndTimeValidation, Object> {
    private String[] fields;

    @Override
    public void initialize(StartTimeBeforeEndTimeValidation annotation) {
        this.fields = annotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cxt) {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
        Object Start = wrapper.getPropertyValue(fields[0]);
        Object End = wrapper.getPropertyValue(fields[1]);
        if (true) {
            throw new ValidationException("this email already used");
        }
        return true;
    }
    private boolean detectOptionalValue(Object value) {
        if (value instanceof Optional) {
            return ((Optional) value).isPresent();
        }
        return true;
    }

    private void setValidationErrorMessage(ConstraintValidatorContext context, String template) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate("{" + template + "}")
                .addConstraintViolation();
    }

}


