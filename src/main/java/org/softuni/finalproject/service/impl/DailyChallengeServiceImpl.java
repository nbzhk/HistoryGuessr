package org.softuni.finalproject.service.impl;

import org.softuni.finalproject.model.dto.ChallengeParticipantDTO;
import org.softuni.finalproject.model.dto.CurrentParticipantDataDTO;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.model.entity.DailyChallengeEntity;
import org.softuni.finalproject.repository.DailyChallengeRepository;
import org.softuni.finalproject.service.DailyChallengeService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Service
public class DailyChallengeServiceImpl implements DailyChallengeService {


    private final RestClient restClient;
    private final DailyChallengeRepository dailyChallengeRepository;

    public DailyChallengeServiceImpl(RestClient restClient, DailyChallengeRepository dailyChallengeRepository) {
        this.restClient = restClient;
        this.dailyChallengeRepository = dailyChallengeRepository;
    }


    @Override
    public DailyChallengeDTO getDailyChallenge() {

        return this.restClient
                .get()
                .uri("http://localhost:8080/challenge/current")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(DailyChallengeDTO.class);

    }

    @Override
    public CurrentParticipantDataDTO getCurrentChallengerData(String username) {

        return this.restClient
                .get()
                .uri("http://localhost:8080/challenge/for-user/{username}", username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(CurrentParticipantDataDTO.class);

    }

    @Override
    public void setParticipant(DailyChallengeDTO dailyChallengeDTO) {

        this.restClient
                .post()
                .uri("http://localhost:8080/challenge/add-participant")
                .body(dailyChallengeDTO)
                .retrieve();

    }

    @Override
    public boolean userAlreadyParticipated() {
        DailyChallengeEntity byDate = dailyChallengeRepository.findByDate(LocalDate.now());
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        return byDate.getParticipants()
                .stream()
                .anyMatch(p -> p.getUser().getUsername().equals(currentUser));
    }
}
