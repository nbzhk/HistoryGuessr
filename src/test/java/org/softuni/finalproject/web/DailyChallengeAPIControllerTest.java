package org.softuni.finalproject.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.model.dto.ChallengeParticipantDTO;
import org.softuni.finalproject.model.dto.CurrentParticipantDataDTO;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DailyChallengeAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailyChallengeAPIService dailyChallengeAPIService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDailyChallenge() throws Exception {
        this.mockMvc.perform(get("/challenge/create"))
                .andExpect(status().isOk());

        verify(this.dailyChallengeAPIService, times(1)).create();
    }

    @Test
    void testGetDailyChallenges() throws Exception {
        DailyChallengeDTO dailyChallengeDTO = new DailyChallengeDTO();

        when(this.dailyChallengeAPIService.getCurrentChallenge()).thenReturn(dailyChallengeDTO);

        this.mockMvc.perform(get("/challenge/current"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(dailyChallengeAPIService, times(1)).getCurrentChallenge();
    }

    @Test
    void testGetDailyChallenge_NotFound() throws Exception {
        when(dailyChallengeAPIService.getCurrentChallenge()).thenReturn(null);

        mockMvc.perform(get("/challenge/current"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Daily challenge not found"));

        verify(dailyChallengeAPIService, times(1)).getCurrentChallenge();
    }

    @Test
    void testGetForUser() throws Exception {
        CurrentParticipantDataDTO currentParticipantDataDTO = new CurrentParticipantDataDTO();

        when(this.dailyChallengeAPIService.getForCurrentUser(anyString())).thenReturn(currentParticipantDataDTO);

        this.mockMvc.perform(get("/challenge/for-user/testUser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(this.dailyChallengeAPIService, times(1)).getForCurrentUser("testUser");
    }

    @Test
    void testGetUserParticipated() throws Exception {
        ChallengeParticipantDTO challengeParticipantDTO = new ChallengeParticipantDTO();
        when(this.dailyChallengeAPIService.userAlreadyParticipated()).thenReturn(challengeParticipantDTO);

        this.mockMvc.perform(get("/challenge/challenge/user-already-participated"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(this.dailyChallengeAPIService, times(1)).userAlreadyParticipated();
    }
}