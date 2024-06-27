package org.softuni.finalproject.validation.picture;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DecimalPlacesValidator implements ConstraintValidator<DecimalPlaces, Double> {

    private static final Integer MAX_DECIMAL_PLACES = 6;

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        String valueStr = String.valueOf(value);
        int decimalIndex = valueStr.indexOf(".");

        return decimalIndex == -1 || valueStr.substring(decimalIndex + 1).length() <= MAX_DECIMAL_PLACES;
    }
}
