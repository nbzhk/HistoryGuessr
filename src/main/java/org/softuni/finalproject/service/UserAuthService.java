package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.LoggedUserDTO;

import java.time.LocalDateTime;
import java.util.Map;

public interface UserAuthService {

    String getCurrentUsername();

    LoggedUserDTO getUserInformation(String username);

    Map<LocalDateTime, Integer> getBestGames(String username);
}
