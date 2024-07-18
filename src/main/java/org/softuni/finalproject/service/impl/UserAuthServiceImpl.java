package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.LoggedUserDTO;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.exception.UserNotFound;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final ModelMapper modelMapper;

    public UserAuthServiceImpl(UserRepository userRepository, GameSessionRepository gameSessionRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }

        throw new UserNotFound("No such user");
    }

    @Override
    public LoggedUserDTO getUserInformation(String username) {

        Optional<UserEntity> user = this.userRepository.findByUsername(username);

        return user.map(userEntity -> this.modelMapper.map(userEntity, LoggedUserDTO.class)).orElse(null);

    }

    @Override
    public Map<LocalDateTime, Integer> getBestGames(String username) {
        Long playerId = getCurrentUserId(username);

        List<Object[]> bestGames = this.gameSessionRepository.findTopFiveGamesForPlayer(playerId);

        return mapData(bestGames);
    }

    private Long getCurrentUserId(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("No such user"));

        return userEntity.getId();
    }

    private Map<LocalDateTime, Integer> mapData(List<Object[]> byPlayerId) {

        Map<LocalDateTime, Integer> map = new LinkedHashMap<>();

        for (Object[] result : byPlayerId) {
            LocalDateTime timeStamp = (LocalDateTime) result[0];
            Integer score = ((Number) result[1]).intValue();
            map.put(timeStamp, score);
        }

        return map;

    }

}
