package org.softuni.finalproject.Init;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DailyChallengeScheduler {


    private final RestClient restClient;

    public DailyChallengeScheduler(RestClient restClient) {
        this.restClient = restClient;

    }


//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 5000)
    public void createDailyChallenge() {
        this.restClient
                .get()
                .uri("http://localhost:8080/challenge/create")
                .retrieve()
                .toBodilessEntity();
    }

}
