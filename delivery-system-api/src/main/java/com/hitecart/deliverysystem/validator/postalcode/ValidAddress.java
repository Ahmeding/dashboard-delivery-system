package com.hitecart.deliverysystem.validator.postalcode;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AddressValidator.class)
public @interface ValidAddress {
    String message() default "Invalid postal code for country";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
