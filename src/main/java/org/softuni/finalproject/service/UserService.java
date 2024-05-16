package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.UserInfoDTO;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;

import java.util.List;

public interface UserService {

    void registerUser(UserRegistrationDTO userRegistrationDTO);

    List<UserInfoDTO> getAllUsers();

    void deleteUser(String username);
}
