package com.example.team_12_be.global.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = HalfOrWholeNumberValidator.class)
public @interface HalfOrWholeNumber {

    String message() default "Must be a whole number or a half number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}