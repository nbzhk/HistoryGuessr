package org.softuni.finalproject.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private User testUser;

    private GameSessionDTO validGame;
    private GameSessionDTO invalidGame;

    @BeforeEach
    void setUp() {
        this.testUser = new User("test", "test", new ArrayList<>());
        UserGuessDTO validGuess = new UserGuessDTO();
        validGuess.setGuessYear(2024);
        validGuess.setGuessLat(0);
        validGuess.setGuessLng(0);

        this.validGame = new GameSessionDTO(testUser, new PictureLocationDTO[5]);
        this.validGame.setRound(1);
        this.validGame.setUserGuesses(new UserGuessDTO[5]);
        this.validGame.getUserGuesses()[0] = validGuess;

        this.invalidGame = new GameSessionDTO(null, null);
    }

    @Test
    void testGetResult_WhenGameSessionIsValid_ReturnsResultView() throws Exception {
        when(this.gameService.getCurrentLocation(any(GameSessionDTO.class), any(Integer.class)))
                .thenReturn(new PictureLocationDTO());

        mockMvc.perform(get("/result")
                        .sessionAttr("gameSession", validGame)
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("currentLocation"))
                .andExpect(model().attributeExists("roundNumber"))
                .andExpect(model().attributeExists("currentGame"))
                .andExpect(model().attributeExists("apiKey"));

    }

    @Test
    void testGetResult_WhenGameSessionIsInvalid_ShouldStartNewGame() throws Exception {
        this.mockMvc.perform(get("/result")
                .sessionAttr("gameSession", invalidGame)
                        .with(user(testUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/start-new-game"));
    }

    @Test
    void testNextRound_ShouldReturnGameView_WhenSessionIsValid() throws Exception {
        when(gameService.getCurrentLocation(any(GameSessionDTO.class), any(Integer.class)))
                .thenReturn(new PictureLocationDTO());

        mockMvc.perform(get("/next-round")
                        .sessionAttr("gameSession", validGame)
                        .with(user(testUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game"));
    }

    @Test
    void testNextRound_ShouldReturnSummaryView_WhenSessionIsValid_AndRoundIsLast() throws Exception {
        when(gameService.getCurrentLocation(any(GameSessionDTO.class), any(Integer.class)))
                .thenReturn(new PictureLocationDTO());

        this.validGame.setRound(5);
        this.validGame.getUserGuesses()[4] = new UserGuessDTO();

        mockMvc.perform(get("/next-round")
                        .sessionAttr("gameSession", validGame)
                        .with(user(testUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/summary"));
    }

}