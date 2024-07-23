package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.ChallengeParticipantDTO;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.softuni.finalproject.service.DailyChallengeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Comparator;
import java.util.List;

@Controller
public class DailyChallengeClientController {
    @Value("${google.maps.key}")
    private String googleMapsKey;

    private final DailyChallengeService dailyChallengeService;
    private final DailyChallengeAPIService dailyChallengeAPIService;

    public DailyChallengeClientController(DailyChallengeService dailyChallengeService, DailyChallengeAPIService dailyChallengeAPIService) {
        this.dailyChallengeService = dailyChallengeService;
        this.dailyChallengeAPIService = dailyChallengeAPIService;
    }

    @ModelAttribute("dailyChallenge")
    public DailyChallengeDTO dailyChallengeDTO() {
        return this.dailyChallengeService.getDailyChallenge();
    }

    @GetMapping("/daily")
    public String daily(Model model) {

        if (this.dailyChallengeService.userAlreadyParticipated()) {
            return "redirect:/daily/table";
        }

       this.dailyChallengeAPIService.addParticipant(dailyChallengeDTO());

        model.addAttribute("imageUrl", dailyChallengeDTO().getPicture().getUrl());
        model.addAttribute("isDaily", true);
        model.addAttribute("apiKey", googleMapsKey);

        return "game";
    }

    @GetMapping("/daily/result")
    public String result(Model model) {

        if (!this.dailyChallengeService.userAlreadyParticipated()) {
            return "redirect:/daily";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        double distance = this.dailyChallengeAPIService.currentGuessDistance(dailyChallengeDTO(), username);

        model.addAttribute("currentChallengerData", dailyChallengeService.getCurrentChallengerData(username));
        model.addAttribute("imageUrl", dailyChallengeDTO().getPicture().getUrl());
        model.addAttribute("dailyDistance", distance);
        model.addAttribute("isDaily", true);
        model.addAttribute("apiKey", googleMapsKey);

        return "result";
    }

    @GetMapping("/daily/table")
    public String showDailyTable(Model model) {

        if (!this.dailyChallengeService.userAlreadyParticipated()) {
            return "redirect:/daily";
        }

        List<ChallengeParticipantDTO> participantsByScore = this.dailyChallengeDTO().getParticipants()
                .stream()
                .sorted(Comparator.comparing(ChallengeParticipantDTO::getScore)
                .reversed())
                .toList();

        model.addAttribute("sortedParticipants", participantsByScore);

        return "daily-table";
    }
}
