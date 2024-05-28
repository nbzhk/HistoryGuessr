package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.service.GameSessionService;
import org.softuni.finalproject.service.PictureService;
import org.softuni.finalproject.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public GameSessionServiceImpl(GameSessionRepository gameSessionRepository, UserService userService, PictureService pictureService, ModelMapper modelMapper) {
        this.gameSessionRepository = gameSessionRepository;
        this.userService = userService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }


    //TODO: check mapping
    @Override
    public void saveGameSession(GameDTO gameDTO) {
        UserEntity user = getUser(gameDTO);
        List<PictureEntity> currentGamePictures = this.pictureService.getCurrentGamePictures(gameDTO.getPictureLocations());
        GameSessionEntity gameSession = map(gameDTO);
        gameSession.setPlayer(user);
        gameSession.setPictures(currentGamePictures);
        gameSession.setGuesses(Arrays.stream(gameDTO.getUserGuesses()).toList());
        gameSession.setRoundsScores(Arrays.stream(gameDTO.getScore()).boxed().toList());
        gameSession.setTimestamp(LocalDateTime.now());
        this.gameSessionRepository.save(gameSession);

    }

    private UserEntity getUser(GameDTO gameDTO) {
        User user = gameDTO.getUser();
        Optional<UserEntity> userEntity = this.userService.findByUsername(user.getUsername());

        return userEntity.orElse(null);
    }

    private GameSessionEntity map(GameDTO gameDTO) {
        return this.modelMapper.map(gameDTO, GameSessionEntity.class);
    }
}
