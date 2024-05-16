package org.softuni.finalproject.model.dto;

import org.softuni.finalproject.model.enums.UserRoleEnum;

import java.time.LocalDate;
import java.util.List;

public class UserInfoDTO {
    private String username;
    private LocalDate registrationDate;
    private List<UserRoleEnum> userRoles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registeredOn) {
        this.registrationDate = registeredOn;
    }

    public List<UserRoleEnum> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleEnum> userRoles) {
        this.userRoles = userRoles;
    }
}
