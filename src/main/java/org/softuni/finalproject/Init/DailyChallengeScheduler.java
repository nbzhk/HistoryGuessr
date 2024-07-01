package org.softuni.finalproject.Init;

import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyChallengeScheduler {

    private final DailyChallengeAPIService dailyChallengeAPIService;

    public DailyChallengeScheduler(DailyChallengeAPIService dailyChallengeAPIService) {
        this.dailyChallengeAPIService = dailyChallengeAPIService;
    }


//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 5000)
    public void createDailyChallenge() {
        this.dailyChallengeAPIService.create();
    }

}
