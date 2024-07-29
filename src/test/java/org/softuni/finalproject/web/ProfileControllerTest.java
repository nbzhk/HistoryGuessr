package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softuni.finalproject.model.dto.*;
import org.softuni.finalproject.service.GameSessionService;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthService userAuthService;

    @MockBean
    private UserService userService;

    @MockBean
    private GameSessionService gameSessionService;

    @MockBean
    private User testUser;

    private LoggedUserDTO loggedUserDTO;
    private GameSessionDTO validGame;
    private Map<GameSessionDTO, Integer> bestGames;

    @BeforeEach
    void setUp() {
        this.testUser = new User("test", "test", new ArrayList<>());

        this.loggedUserDTO = new LoggedUserDTO();
        this.loggedUserDTO.setUsername(testUser.getUsername());
        this.loggedUserDTO.setEmail("test@test.com");
        this.loggedUserDTO.setRegistrationDate(LocalDate.now());

        UserGuessDTO validGuess = new UserGuessDTO();
        validGuess.setGuessYear(2024);
        validGuess.setGuessLat(0);
        validGuess.setGuessLng(0);

        this.validGame = new GameSessionDTO(testUser, new PictureLocationDTO[5]);
        this.validGame.setRound(1);
        this.validGame.setUserGuesses(new UserGuessDTO[5]);
        this.validGame.getUserGuesses()[0] = validGuess;

        this.bestGames = new LinkedHashMap<>();
        bestGames.put(validGame, 10000);
    }

    @Test
    void testGetProfile_WithUser_ShouldReturnProfileView() throws Exception {
        when(this.userAuthService.getUserInformation(anyString())).thenReturn(this.loggedUserDTO);

        this.mockMvc.perform(get("/profile")
                .with(user(this.testUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("userInformation"));
    }

    @Test
    void testBestGames_ShouldReturnSummaryView() throws Exception {
        when(this.userService.getBestGames(anyString())).thenReturn(this.bestGames);

        this.mockMvc.perform(get("/profile/best-game/summary/1")
                .with(user(this.testUser))
                .sessionAttr("gameSession", validGame))
                .andExpect(status().isOk())
                .andExpect(view().name("summary"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attributeExists("totalScore"))
                .andExpect(model().attributeExists("apiKey"))
                .andExpect(model().attributeExists("fromBest"));
    }

    @Test
    void testBestGameRound_ShouldReturnResultView() throws Exception {
        PictureLocationDTO picture = new PictureLocationDTO();
        picture.setUrl("testUrl");
        picture.setDescription("testDescription");
        picture.setLatitude(0);
        picture.setLongitude(0);
        picture.setYear(2024);

        UserGuessDTO userGuess = new UserGuessDTO();
        userGuess.setGuessLat(0);
        userGuess.setGuessLng(0);
        userGuess.setGuessYear(2024);

        RoundInfoDTO roundInfoDTO = new RoundInfoDTO();
        roundInfoDTO.setScore(10000);
        roundInfoDTO.setPictureLocationDTO(picture);
        roundInfoDTO.setDistanceDiff(0);
        roundInfoDTO.setYearDiff(0);
        roundInfoDTO.setUserGuessDTO(userGuess);

        when(this.gameSessionService.getRoundInfo(anyInt(), any(HttpSession.class)))
                .thenReturn(roundInfoDTO);

        this.mockMvc.perform(get("/profile/best-game/summary/round=1")
                .with(user(this.testUser))
                .sessionAttr("gameSession", validGame))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("roundInfo"))
                .andExpect(model().attributeExists("currentLocation"))
                .andExpect(model().attributeExists("roundData"))
                .andExpect(model().attributeExists("apiKey"));

    }

}