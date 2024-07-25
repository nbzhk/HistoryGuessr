package org.softuni.finalproject.service.impl;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.model.dto.*;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private PictureService pictureService;

    @Mock
    private DailyChallengeAPIService dailyChallengeAPIService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private GameServiceImpl gameServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testStartGame() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User("testUser", "testPass", new ArrayList<>());
        when(authentication.getPrincipal()).thenReturn(user);

        PictureLocationDTO[] pictureLocationDTOS = new PictureLocationDTO[5];
        when(this.pictureService.createPictureLocations()).thenReturn(pictureLocationDTOS);

        GameSessionDTO gameSessionDTO = this.gameServiceImpl.startGame(session);

        assertNotNull(gameSessionDTO);
        assertEquals(user, gameSessionDTO.getUser());
        assertEquals(pictureLocationDTOS, gameSessionDTO.getPictureLocations());
        verify(this.session, times(1)).setAttribute("gameSession", gameSessionDTO);
    }

    @Test
    public void testCalculateRoundScore() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User("testUser", "testPass", new ArrayList<>());
        when(authentication.getPrincipal()).thenReturn(user);

        PictureLocationDTO pictureLocationDTO = new PictureLocationDTO();
        pictureLocationDTO.setUrl("testUrl");
        pictureLocationDTO.setDescription("testDescription");
        pictureLocationDTO.setYear(2000);
        pictureLocationDTO.setLatitude(0);
        pictureLocationDTO.setLongitude(0);

        GameSessionDTO gameSessionDTO = new GameSessionDTO(user, new PictureLocationDTO[] {pictureLocationDTO});
        gameSessionDTO.setRound(1);

        UserGuessDTO userGuessDTO = new UserGuessDTO();
        userGuessDTO.setGuessYear(2005);
        userGuessDTO.setGuessLat(0);
        userGuessDTO.setGuessLng(1.0);

        gameSessionDTO.setUserGuesses(new UserGuessDTO[] {userGuessDTO});

        this.gameServiceImpl.calculateRoundScore(gameSessionDTO);

        assertEquals(5, gameSessionDTO.getRoundYearDifference(1));
        assertTrue(gameSessionDTO.getRoundDistanceDifference(1) > 0);
        assertTrue(gameSessionDTO.getRoundScore(1) > 0);

    }

    @Test
    public void testGetCurrentGame() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User("testUser", "testPass", new ArrayList<>());
        when(authentication.getName()).thenReturn(user.getUsername());

        GameSessionDTO gameSessionDTO = new GameSessionDTO(user, new PictureLocationDTO[0]);
        when(session.getAttribute("gameSession")).thenReturn(gameSessionDTO);

        GameSessionDTO result = this.gameServiceImpl.getCurrentGame(session);

        assertEquals(gameSessionDTO, result);
    }

    @Test
    public void testCalculateDailyScore() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User("testUser", "testPass", new ArrayList<>());
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.getName()).thenReturn(user.getUsername());


        ChallengeParticipantDTO participant = new ChallengeParticipantDTO();
        participant.setUsername(user.getUsername());

        UserGuessDTO userGuessDTO = new UserGuessDTO();
        userGuessDTO.setGuessYear(2000);
        userGuessDTO.setGuessLat(0);
        userGuessDTO.setGuessLng(0.0);

        participant.setUserGuessDTO(userGuessDTO);

        DailyChallengeDTO dailyChallengeDTO = new DailyChallengeDTO();
        dailyChallengeDTO.setParticipants(new ArrayList<>());
        dailyChallengeDTO.getParticipants().add(participant);

        PictureLocationDTO pictureLocationDTO = new PictureLocationDTO();
        pictureLocationDTO.setUrl("testUrl");
        pictureLocationDTO.setDescription("testDescription");
        pictureLocationDTO.setYear(2000);
        pictureLocationDTO.setLatitude(0);
        pictureLocationDTO.setLongitude(0.0);

        dailyChallengeDTO.setPicture(pictureLocationDTO);

        this.gameServiceImpl.calculateDailyScore(dailyChallengeDTO);

        ArgumentCaptor<Integer> scoreCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(this.dailyChallengeAPIService, times(1)).setParticipantScore(eq(participant), scoreCaptor.capture());

        int expectedScore = 10000;

        this.gameServiceImpl.calculateDailyScore(dailyChallengeDTO);

        assertEquals(scoreCaptor.getValue(), expectedScore);




    }

    @Test
    public void testSetDailyGuess() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User("testUser", "testPass", new ArrayList<>());
        when(authentication.getName()).thenReturn(user.getUsername());

        UserGuessDTO userGuessDTO = new UserGuessDTO();
        userGuessDTO.setGuessLat(0.0);
        userGuessDTO.setGuessLng(0.0);
        userGuessDTO.setGuessYear(2000);

        ChallengeParticipantDTO participant = new ChallengeParticipantDTO();
        participant.setUsername(user.getUsername());
        participant.setUserGuessDTO(new UserGuessDTO());

        DailyChallengeDTO dailyChallengeDTO = new DailyChallengeDTO();
        dailyChallengeDTO.setParticipants(new ArrayList<>());
        dailyChallengeDTO.getParticipants().add(participant);

        gameServiceImpl.setDailyGuess(userGuessDTO, dailyChallengeDTO);

        assertEquals(userGuessDTO.getGuessLat(), participant.getUserGuessDTO().getGuessLat());
        assertEquals(userGuessDTO.getGuessLng(), participant.getUserGuessDTO().getGuessLng());
        assertEquals(userGuessDTO.getGuessYear(), participant.getUserGuessDTO().getGuessYear());
        verify(this.dailyChallengeAPIService, times(1)).setUserGuess(participant, participant.getUserGuessDTO());
    }
}
