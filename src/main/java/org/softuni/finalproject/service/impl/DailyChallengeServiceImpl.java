package org.softuni.finalproject.service.impl;

import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.service.DailyChallengeService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DailyChallengeServiceImpl implements DailyChallengeService {


    private final RestClient restClient;

    public DailyChallengeServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public DailyChallengeDTO getDailyChallenge() {
//        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        return this.restClient
                .get()
                .uri("http://localhost:8080/challenge/current")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(DailyChallengeDTO.class);

    }
}
