package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.service.GameService;
import org.softuni.finalproject.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameSessionService gameSessionService;

    @MockBean
    private GameService gameService;

    private GameSessionDTO validGame;
    private GameSessionDTO invalidGame;

    @BeforeEach
    void setUp() {
        User user = new User("test", "test", new ArrayList<>());

        UserGuessDTO[] userGuessesTest = new UserGuessDTO[5];
        for (int i = 0; i < userGuessesTest.length; i++) {

            UserGuessDTO validGuess = new UserGuessDTO();
            validGuess.setGuessYear(2024);
            validGuess.setGuessLat(0);
            validGuess.setGuessLng(0);

            userGuessesTest[i] = validGuess;
        }

        PictureLocationDTO[] pictureLocationsTest = new PictureLocationDTO[5];
        for (int i = 0; i < pictureLocationsTest.length; i++) {

            PictureLocationDTO pictureLocationDTO = new PictureLocationDTO();
            pictureLocationDTO.setUrl("test.url");
            pictureLocationDTO.setDescription("test description");
            pictureLocationDTO.setLatitude(0.0);
            pictureLocationDTO.setLongitude(0.0);
            pictureLocationDTO.setYear(2024);

            pictureLocationsTest[i] = pictureLocationDTO;
        }


        this.validGame = new GameSessionDTO(user, pictureLocationsTest);
        this.validGame.setUserGuesses(userGuessesTest);

        this.invalidGame = new GameSessionDTO(null, null);
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetSummary_WhenSessionIsValid_ShouldReturnSummaryView() throws Exception {

        this.mockMvc.perform(get("/summary")
                        .sessionAttr("gameSession", validGame))
                .andExpect(status().isOk())
                .andExpect(view().name("summary"))
                .andExpect(model().attributeExists("totalScore"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attributeExists("fromBest"))
                .andExpect(model().attributeExists("apiKey"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetSummary_WhenSessionIsInvalid_ShouldRedirectToNewGame() throws Exception {

        this.mockMvc.perform(get("/summary")
                    .sessionAttr("gameSession", invalidGame))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/start-new-game"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testSaveGameSession_ShouldSaveGameSession_AndStartNewGameBasedOnAction() throws Exception {

        this.mockMvc.perform(post("/summary/save")
                        .sessionAttr("gameSession", validGame)
                        .param("action", "newGame")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/start-new-game"));

        verify(gameSessionService).saveGameSession(validGame);

    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testSaveGame_ShouldSaveGame_AndRedirectHome_BasedOnAction() throws Exception {

        this.mockMvc.perform(post("/summary/save")
                        .sessionAttr("gameSession", validGame)
                        .param("action", "home")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(gameSessionService).saveGameSession(validGame);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testShowRoundSummary_ShouldReturnResultView(int round) throws Exception {

        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(validGame);
        when(this.gameService.getCurrentLocation(any(GameSessionDTO.class), anyInt())).thenReturn(validGame.getPictureLocations()[round - 1]);

        this.mockMvc.perform(get("/summary/round={round}", round)
                    .sessionAttr("gameSession", validGame))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("currentLocation"))
                .andExpect(model().attributeExists("roundNumber"))
                .andExpect(model().attributeExists("currentGame"))
                .andExpect(model().attributeExists("apiKey"));

    }
}