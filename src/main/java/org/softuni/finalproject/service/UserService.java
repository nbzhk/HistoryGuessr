package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.UserInfoDTO;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.softuni.finalproject.model.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    boolean register(UserRegistrationDTO userRegistrationDTO);

    List<UserInfoDTO> getAllUsers();

    void delete(String username);

    void promoteUserToAdmin(String username);

    void demoteAdminToUser(String username);

    Map<GameSessionDTO, Integer> getBestGames(String username);

    Optional<UserEntity> findByUsername(String currentUsername);

    void saveUser(UserEntity userEntity);
}
