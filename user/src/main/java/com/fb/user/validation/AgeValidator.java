package com.fb.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {



    @Override

    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {

        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 13;

    }

}
