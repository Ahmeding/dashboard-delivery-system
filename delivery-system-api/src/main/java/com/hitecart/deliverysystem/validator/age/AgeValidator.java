package com.hitecart.deliverysystem.validator.age;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {
    private int minAge;

    @Override
    public void initialize(Age constraint) {
        this.minAge = constraint.min();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        return birthDate != null &&
                Period.between(birthDate, LocalDate.now()).getYears() >= minAge;
    }
}
