package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.LoggedUserDTO;

public interface UserAuthService {

    String getCurrentUsername();

    LoggedUserDTO getUserInformation(String username);
}
