package org.softuni.finalproject.validation.picture;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DecimalPlacesValidator.class)
public @interface DecimalPlaces {
    String message() default "Format to 6 decimal places!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
