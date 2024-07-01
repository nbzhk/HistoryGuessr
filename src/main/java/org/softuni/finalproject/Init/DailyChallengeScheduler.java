package org.softuni.finalproject.Init;

import org.softuni.finalproject.service.DailyChallengeService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyChallengeScheduler {

    private final DailyChallengeService dailyChallengeService;

    public DailyChallengeScheduler(DailyChallengeService dailyChallengeService) {
        this.dailyChallengeService = dailyChallengeService;
    }


//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 5000)
    public void createDailyChallenge() {
        this.dailyChallengeService.create();
    }

}
