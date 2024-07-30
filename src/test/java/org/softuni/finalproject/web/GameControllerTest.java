package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private GameSessionDTO validGame;
    private PictureLocationDTO validPictureLocation;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.testUser = new User("test", "testPass", new ArrayList<>());

        UserGuessDTO validGuess = new UserGuessDTO();
        validGuess.setGuessYear(2024);
        validGuess.setGuessLat(0);
        validGuess.setGuessLng(0);

        this.validGame = new GameSessionDTO(testUser, new PictureLocationDTO[5]);
        this.validGame.setRound(1);
        this.validGame.setUserGuesses(new UserGuessDTO[5]);
        this.validGame.getUserGuesses()[0] = validGuess;

        this.validPictureLocation = new PictureLocationDTO();
        validPictureLocation.setUrl("testUrl");
        validPictureLocation.setYear(2024);
        validPictureLocation.setLatitude(0);
        validPictureLocation.setLongitude(0);
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetView_WhenGameSessionIsNull_ShouldRedirectToStartNewGame() throws Exception {
        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(null);
        when(this.gameService.startGame(any(HttpSession.class))).thenReturn(validGame);

       this.mockMvc.perform(get("/game"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/start-new-game"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGameView_WhenLastRound_ShouldRedirectToResult() throws Exception {

        validGame.setRound(6); //The game has 5 rounds, but on the last one it goes to 6
        when(gameService.getCurrentGame(any(HttpSession.class))).thenReturn(validGame);
        when(gameService.getCurrentLocation(any(GameSessionDTO.class), anyInt())).thenReturn(validPictureLocation);

        mockMvc.perform(get("/game")
                        .sessionAttr("gameSession", validGame)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/result"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetGameView_WhenComingFromResultPage_ShouldRedirectToHome() throws Exception {
        when(gameService.getCurrentGame(any(HttpSession.class))).thenReturn(validGame);
        when(gameService.getCurrentLocation(any(GameSessionDTO.class), anyInt())).thenReturn(validPictureLocation);

        mockMvc.perform(get("/game")
                        .sessionAttr("gameSession", validGame)
                        .sessionAttr("fromResult", true)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetGameView_WithValidGameSession_ShouldGetGameView() throws Exception {
        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(validGame);
        when(this.gameService.getCurrentLocation(any(GameSessionDTO.class), anyInt())).thenReturn(validPictureLocation);

        this.validGame.getPictureLocations()[0] = validPictureLocation;

        mockMvc.perform(get("/game")
                        .sessionAttr("gameSession", validGame))
                .andExpect(status().isOk())
                .andExpect(view().name("game"))
                .andExpect(model().attributeExists("imageUrl"))
                .andExpect(model().attributeExists("roundNumber"))
                .andExpect(model().attributeExists("score"))
                .andExpect(model().attributeExists("apiKey"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testStartNewGame_ShouldRedirectToGame() throws Exception {
        when(gameService.startGame(any(HttpSession.class))).thenReturn(validGame);

        mockMvc.perform(get("/game/start-new-game")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game"));
    }
}