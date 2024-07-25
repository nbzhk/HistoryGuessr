package org.softuni.finalproject.service.impl;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.RoundInfoDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.service.PictureService;
import org.softuni.finalproject.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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

    @Test
    public void testGetRoundInfo() {
        String username = "testUser";
        Long userId = 1L;

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setId(userId);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(userEntity));

        HttpSession httpSession = mock(HttpSession.class);

        PictureLocationDTO[] pictures = {new PictureLocationDTO(), new PictureLocationDTO()};
        UserGuessDTO[] guesses = {new UserGuessDTO(), new UserGuessDTO()};

        User user = new User(username, "testPass", new ArrayList<>());
        when(authentication.getPrincipal()).thenReturn(user);

        GameSessionDTO gameSessionDTO = new GameSessionDTO(user, pictures);
        gameSessionDTO.setUserGuesses(guesses);
        gameSessionDTO.setRoundScores(new int[] { 10, 20 , 30 ,40, 50 });
        gameSessionDTO.setYearDifferences(new int[] { 1, 2 , 3 , 4, 5 });
        gameSessionDTO.setDistanceDifferences(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
        gameSessionDTO.setTimestamp(LocalDateTime.now().toLocalDate());

        when(httpSession.getAttribute("bestGame")).thenReturn(gameSessionDTO);

        int round = 1;
        RoundInfoDTO roundInfo = this.gameSessionService.getRoundInfo(1, httpSession);

        assertNotNull(roundInfo);
        assertEquals(round, roundInfo.getRound());
        assertEquals(pictures[round - 1], roundInfo.getPictureLocationDTO());
        assertEquals(guesses[round - 1], roundInfo.getUserGuessDTO());
        assertEquals(10, roundInfo.getScore());
        assertEquals(1, roundInfo.getYearDiff());
        assertEquals(1.0, roundInfo.getDistanceDiff());
    }
}
