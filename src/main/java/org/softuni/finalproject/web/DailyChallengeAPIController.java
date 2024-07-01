package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge")
public class DailyChallengeAPIController {

    private final DailyChallengeAPIService dailyChallengeAPIService;

    public DailyChallengeAPIController(DailyChallengeAPIService dailyChallengeAPIService) {
        this.dailyChallengeAPIService = dailyChallengeAPIService;
    }

    @GetMapping("/current")
    public DailyChallengeDTO getDailyChallenge() {
        return this.dailyChallengeAPIService.getCurrentChallenge();
    }
}
