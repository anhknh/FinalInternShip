package com.example.finalinternship.exception.CustomValidation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = StatusValidator.class)
public @interface Status {
    String message() default "Status is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
