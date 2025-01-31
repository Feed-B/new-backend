package com.example.team_12_be.global.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HalfOrWholeNumberValidator implements ConstraintValidator<HalfOrWholeNumber, Float> {

    @Override
    public void initialize(HalfOrWholeNumber constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        float remainder = value % 1;
        return remainder == 0.0f || remainder == 0.5f;
    }
}