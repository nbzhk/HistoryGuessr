package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.RoundInfoDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.model.entity.BaseEntity;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.service.GameSessionService;
import org.softuni.finalproject.service.PictureService;
import org.softuni.finalproject.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public GameSessionServiceImpl(GameSessionRepository gameSessionRepository, UserService userService, ModelMapper modelMapper, PictureService pictureService) {
        this.gameSessionRepository = gameSessionRepository;
        this.userService = userService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }


    @Override
    public void saveGameSession(GameSessionDTO gameSessionDTO) {
        UserEntity user = getUser(gameSessionDTO);
        List<PictureEntity> currentGamePictures = this.pictureService.getCurrentGamePictures(gameSessionDTO.getPictureLocations());
        GameSessionEntity gameSession = map(gameSessionDTO);

        gameSession.setPlayer(user);
        gameSession.setPictures(currentGamePictures);
        gameSession.setTimestamp(LocalDateTime.now());
        this.gameSessionRepository.save(gameSession);

    }

    @Override
    public RoundInfoDTO getRoundInfo(int round) {
        Long userId = getUserId();
        if (userId == null) {
            return null;
        }

        GameSessionEntity game = this.gameSessionRepository.findTopByPlayerIdOrderByTimestampDesc(userId);

        RoundInfoDTO roundInfoDTO = new RoundInfoDTO();
        roundInfoDTO.setRound(round);
        PictureLocationDTO roundPicture = this.modelMapper.map(game.getPictures().get(round - 1), PictureLocationDTO.class);
        roundInfoDTO.setPictureLocationDTO(roundPicture);
        UserGuessDTO roundGuess = this.modelMapper.map(game.getGuesses().get(round - 1), UserGuessDTO.class);
        roundInfoDTO.setUserGuessDTO(roundGuess);

        return roundInfoDTO;
    }

    private Long getUserId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> user = userService.findByUsername(name);
        return user.map(BaseEntity::getId).orElse(null);
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
