package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.model.entity.DailyChallengeEntity;
import org.softuni.finalproject.service.DailyChallengeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge")
public class DailyChallengeController {

    private final DailyChallengeService dailyChallengeService;

    public DailyChallengeController(DailyChallengeService dailyChallengeService) {
        this.dailyChallengeService = dailyChallengeService;
    }

    @PostMapping("/create")
    public void createChallenge() {
        this.dailyChallengeService.create();
    }
}
