package com.example.accommodiq.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class FutureLongDateValidator implements ConstraintValidator<FutureLongDate, Long> {
    @Override
    public void initialize(FutureLongDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        return aLong > Instant.now().toEpochMilli();
    }
}
