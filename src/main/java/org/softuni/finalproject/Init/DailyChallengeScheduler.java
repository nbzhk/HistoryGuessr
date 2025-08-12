package org.softuni.finalproject.Init;

import jakarta.annotation.PostConstruct;
import org.softuni.finalproject.repository.DailyChallengeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Component
public class DailyChallengeScheduler {


    private final RestClient restClient;
    private final DailyChallengeRepository dailyChallengeRepository;

    public DailyChallengeScheduler(RestClient restClient, DailyChallengeRepository dailyChallengeRepository) {
        this.restClient = restClient;
        this.dailyChallengeRepository = dailyChallengeRepository;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    // @Scheduled(cron = "0 * * * * *")
    public void createDailyChallenge() {
        if (!dailyChallengeRepository.existsByDate(LocalDate.now())) {
            this.restClient
                    .get()
                    .uri(System.getenv("BASE_URL") + "/challenge/create")
                    .retrieve()
                    .toBodilessEntity();
        }
    }

    //Creates dailyChallenge on startup
    @PostConstruct
    public void initDailyChallengeOnStartup() {
        createDailyChallenge();
    }
}
