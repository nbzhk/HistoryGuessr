package org.softuni.finalproject.model.dto;

import java.time.LocalDate;

public class LoggedUserDTO {

    private String username;
    private LocalDate registrationDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}
