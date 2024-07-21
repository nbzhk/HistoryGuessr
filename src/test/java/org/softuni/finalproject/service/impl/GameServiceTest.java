package org.softuni.finalproject.service.impl;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.softuni.finalproject.service.DailyChallengeService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private PictureService pictureService;

    @Mock
    private DailyChallengeAPIService dailyChallengeApiService;

    @Mock
    private HttpSession httpSession;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;


    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("testUser", "password", new ArrayList<>());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testStartGame(){
        User mockUser = new User("testUser", "password", new ArrayList<>());
        GameSessionDTO expectedGameSession = new GameSessionDTO(mockUser, null);

        GameSessionDTO actualGameSession = this.gameService.startGame(httpSession);

        verify(httpSession).setAttribute(eq("gameSession"), any(GameSessionDTO.class));
        assertEquals(mockUser.getUsername(),actualGameSession.getUser().getUsername());

    }

}
