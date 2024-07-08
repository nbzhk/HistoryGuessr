package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.service.DailyChallengeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DailyChallengeController {
    @Value("${google.maps.key}")
    private String googleMapsKey;

    private final DailyChallengeService dailyChallengeService;

    public DailyChallengeController(DailyChallengeService dailyChallengeService) {
        this.dailyChallengeService = dailyChallengeService;
    }

    @ModelAttribute("dailyChallenge")
    public DailyChallengeDTO dailyChallengeDTO() {
        return this.dailyChallengeService.getDailyChallenge();
    }

    @GetMapping("/daily")
    public String daily(Model model) {

        model.addAttribute("imageUrl", dailyChallengeDTO().getPicture().getUrl());
        model.addAttribute("apiKey", googleMapsKey);

        return "daily";
    }

    @GetMapping("/daily/result")
    public String result(Model model) {

        model.addAttribute("imageUrl", dailyChallengeDTO().getPicture().getUrl());

        return "daily-result";
    }

    @PostMapping("/daily/make-guess")
    public String makeGuess() {
        return "daily";
    }
}
