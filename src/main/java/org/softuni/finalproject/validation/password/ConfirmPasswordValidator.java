package org.softuni.finalproject.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, UserRegistrationDTO> {
    @Override
    public boolean isValid(UserRegistrationDTO userRegistrationDTO, ConstraintValidatorContext context) {
        return userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword());
    }
}
