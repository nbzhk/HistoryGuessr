package org.softuni.finalproject.validation.user;



import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;


public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, UserRegistrationDTO> {
    @Override
    public boolean isValid(UserRegistrationDTO userRegistrationDTO, ConstraintValidatorContext context) {

        boolean isValid = userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
