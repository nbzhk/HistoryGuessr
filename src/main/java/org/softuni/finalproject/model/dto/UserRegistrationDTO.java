package org.softuni.finalproject.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.softuni.finalproject.validation.user.ConfirmPassword;
import org.softuni.finalproject.validation.user.UniqueEmail;
import org.softuni.finalproject.validation.user.UniqueUsername;

import java.time.LocalDate;

@ConfirmPassword
public class UserRegistrationDTO {

    @Size(min = 4, max = 20, message = "{register.username.size}")
    @UniqueUsername
    @NotBlank(message = "{register.username.blank}")
    private String username;
    @Email(message = "{register.email.invalid}")
    @NotBlank(message = "{register.email.blank}")
    @UniqueEmail
    private String email;
    @Size(min = 5, message = "{register.password.size}")
    private String password;
    @Size(min = 5, message = "{register.password.size}")
    private String confirmPassword;
    private LocalDate registrationDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

}
