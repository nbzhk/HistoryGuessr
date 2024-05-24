package org.softuni.finalproject.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        UserRegistrationDTO userRegistrationDTO = (UserRegistrationDTO) object;
        boolean valid = userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword());

        if (!valid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return valid;
    }
}
