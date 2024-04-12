package com.example.project_sem4_springboot_api.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

/**
 * The interface Valid date range.
 */
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RUNTIME)
public @interface ValidDateRange {

    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "fromDate cannot be after toDate";

    /**
     * From date string.
     *
     * @return the string
     */
    String fromDate();

    /**
     * To date string.
     *
     * @return the string
     */
    String toDate();

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Payload>[] payload() default {};
}