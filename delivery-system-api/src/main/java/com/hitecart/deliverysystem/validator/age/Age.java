package com.hitecart.deliverysystem.validator.age;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface Age {
    String message() default "Invalid age";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 18;
}
