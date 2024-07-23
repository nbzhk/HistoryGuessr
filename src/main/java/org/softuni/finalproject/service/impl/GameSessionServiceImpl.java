package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.service.GameSessionService;
import org.softuni.finalproject.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public GameSessionServiceImpl(GameSessionRepository gameSessionRepository, UserService userService, ModelMapper modelMapper) {
        this.gameSessionRepository = gameSessionRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    
    @Override
    public void saveGameSession(GameSessionDTO gameSessionDTO) {
        UserEntity user = getUser(gameSessionDTO);
        GameSessionEntity gameSession = map(gameSessionDTO);

        gameSession.setPlayer(user);
        gameSession.setTimestamp(LocalDateTime.now());
        this.gameSessionRepository.save(gameSession);

    }

    private UserEntity getUser(GameSessionDTO gameSessionDTO) {
        User user = gameSessionDTO.getUser();
        Optional<UserEntity> userEntity = this.userService.findByUsername(user.getUsername());

        return userEntity.orElse(null);
    }

    private GameSessionEntity map(GameSessionDTO gameSessionDTO) {
        return this.modelMapper.map(gameSessionDTO, GameSessionEntity.class);
    }
}
