package com.example.finalinternship.exception.CustomValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class StatusValidator implements ConstraintValidator<Status, Integer> {

    List<Integer> statusList = Arrays.asList(0,1);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return statusList.contains(value);
    }
}
