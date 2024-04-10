package com.example.project_sem4_springboot_api.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

/**
 * The type Valid date range validator.
 */
public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String fromDate;

    private String toDate;

    private String message;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.fromDate = constraintAnnotation.fromDate();
        this.toDate = constraintAnnotation.toDate();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime fromDateValue = (LocalDateTime) new BeanWrapperImpl(o)
                .getPropertyValue(fromDate);
        LocalDateTime toDateValue = (LocalDateTime) new BeanWrapperImpl(o)
                .getPropertyValue(toDate);

        if (fromDateValue == null || toDateValue == null) {
            return true;
        }
        if (fromDateValue.isAfter(toDateValue)) {
            String errorMessage = message.replace("fromDate", this.fromDate).replace("toDate", this.toDate);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }

        return true;
    }
}