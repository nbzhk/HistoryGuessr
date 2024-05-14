package org.softuni.finalproject.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.softuni.finalproject.validation.UniqueUsername;

import java.time.LocalDate;

public class UserRegistrationDTO {

    @Size(min = 4, max = 20, message = "Username should be between 4 and 20 symbols")
    @UniqueUsername
    private String username;
    @Size(min = 5, message = "Password should have at least 5 symbols")
    private String password;
    @Email
    private String email;
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
