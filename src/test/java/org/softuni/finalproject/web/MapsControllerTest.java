package org.softuni.finalproject.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.model.dto.*;
import org.softuni.finalproject.service.DailyChallengeService;
import org.softuni.finalproject.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class MapsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private DailyChallengeService dailyChallengeService;

    private GameSessionDTO gameSessionDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User("test", "testPass", new ArrayList<>());

        UserGuessDTO[] userGuesses = new UserGuessDTO[5];
        for (int i = 0; i < userGuesses.length; i++) {
            UserGuessDTO userGuessDTO = new UserGuessDTO();
            userGuessDTO.setGuessYear(2024);
            userGuessDTO.setGuessLat(0);
            userGuessDTO.setGuessLng(0);
            userGuesses[i] = userGuessDTO;
        }

        PictureLocationDTO[] pictureLocations = new PictureLocationDTO[5];
        for (int i = 0; i < pictureLocations.length; i++) {
            PictureLocationDTO pictureLocation = new PictureLocationDTO();
            pictureLocation.setUrl("test.url");
            pictureLocation.setDescription("test description");
            pictureLocation.setLatitude(0.0);
            pictureLocation.setLongitude(0.0);
            pictureLocation.setYear(2024);
            pictureLocations[i] = pictureLocation;

            this.gameSessionDTO = new GameSessionDTO(user, pictureLocations);
            this.gameSessionDTO.setUserGuesses(userGuesses);

        }
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetUserGuess_ShouldReturnUserGuessJSON() throws Exception {

        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(this.gameSessionDTO);

        this.mockMvc.perform(post("/game/get-user-guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(this.gameSessionDTO.getUserGuesses()[0]))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(this.gameSessionDTO.getUserGuesses()[0])));

    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetResult_ShouldReturnGameSessionAsJson() throws Exception {
        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(this.gameSessionDTO);

        this.mockMvc.perform(post("/game/get-result")
                    .sessionAttr("gameSession", this.gameSessionDTO)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(this.gameSessionDTO)));

    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGameSummary_ShouldReturnGameSessionJson() throws Exception {
        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(this.gameSessionDTO);

        this.mockMvc.perform(post("/game/summary")
                        .sessionAttr("gameSession", this.gameSessionDTO)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(this.gameSessionDTO)));
    }
    
    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetBestGame_ShouldReturnBestGameJson() throws Exception {
        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(this.gameSessionDTO);

        this.mockMvc.perform(post("/profile/best")
                        .sessionAttr("bestGame", this.gameSessionDTO)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(this.gameSessionDTO)));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testDailyGuess_ShouldReturnUserGuessDTOJson() throws Exception {
        UserGuessDTO userGuessDTO = new UserGuessDTO();
        userGuessDTO.setGuessYear(2000);
        userGuessDTO.setGuessLat(1);
        userGuessDTO.setGuessLng(1);

        DailyChallengeDTO dailyChallengeDTO = new DailyChallengeDTO();
        dailyChallengeDTO.setPicture(new PictureLocationDTO());
        dailyChallengeDTO.setParticipants(new ArrayList<>());

        when(this.dailyChallengeService.getDailyChallenge()).thenReturn(dailyChallengeDTO);

        this.mockMvc.perform(post("/daily/make-guess")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(userGuessDTO))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(userGuessDTO)));

    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testDailyGetResult_ShouldReturnCurrentParticipantDataDTO() throws Exception {
        UserGuessDTO dailyGuessTest = new UserGuessDTO();
        dailyGuessTest.setGuessYear(2024);
        dailyGuessTest.setGuessLat(0);
        dailyGuessTest.setGuessLng(0);

        PictureLocationDTO dailyPictureTest = new PictureLocationDTO();
        dailyPictureTest.setUrl("test.url");
        dailyPictureTest.setDescription("test test test");
        dailyPictureTest.setLatitude(0);
        dailyPictureTest.setLongitude(0);
        dailyPictureTest.setYear(2024);

        CurrentParticipantDataDTO currentParticipantDataDTO = new CurrentParticipantDataDTO();
        currentParticipantDataDTO.setUsername("test");
        currentParticipantDataDTO.setUserGuessDTO(dailyGuessTest);
        currentParticipantDataDTO.setPicture(dailyPictureTest);
        currentParticipantDataDTO.setScore(0);

        when(this.dailyChallengeService.getCurrentChallengerData(anyString())).thenReturn(currentParticipantDataDTO);

        MockHttpSession session = new MockHttpSession();

        this.mockMvc.perform(post("/daily/get-result")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(currentParticipantDataDTO)));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetRoundInfo_ShouldReturnRoundInfoDTO(int round) throws Exception {

        when(this.gameService.getCurrentGame(any(HttpSession.class))).thenReturn(this.gameSessionDTO);

        this.mockMvc.perform(post("/summary/round-info/{round}", round)
                        .sessionAttr("bestGame", this.gameSessionDTO)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.round").value(round))
                .andExpect(jsonPath("$.userGuessDTO").isNotEmpty())
                .andExpect(jsonPath("$.pictureLocationDTO").isNotEmpty());
    }

    private String asJsonString(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(o);
    }

}