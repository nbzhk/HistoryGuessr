package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.service.PictureService;
import org.softuni.finalproject.service.UserService;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameSessionServiceTest {

    @Mock
    private GameSessionRepository gameSessionRepository;

    @Mock
    private UserService userService;

    @Mock
    private PictureService pictureService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GameSessionServiceImpl gameSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveGameSession() {
        User user = new User("testUser","testPassword", Collections.emptyList());
        PictureLocationDTO pictureLocationDTO = new PictureLocationDTO();
        pictureLocationDTO.setUrl("testUrl");
        PictureLocationDTO[] pictureLocationDTOs = {pictureLocationDTO};

        GameSessionDTO gameSessionDTO = new GameSessionDTO(user, pictureLocationDTOs);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        GameSessionEntity gameSessionEntity = new GameSessionEntity();
        gameSessionEntity.setPlayer(userEntity);

        when(modelMapper.map(gameSessionDTO, GameSessionEntity.class)).thenReturn(gameSessionEntity);

        when(userService.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        gameSessionService.saveGameSession(gameSessionDTO);

        verify(gameSessionRepository).save(gameSessionEntity);
    }
}
