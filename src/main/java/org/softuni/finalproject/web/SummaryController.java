package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Objects;

@Controller
public class SummaryController {

    private final GameSessionService gameSessionService;
    @Value("${google.maps.key}")
    private String googleMapsKey;

    public SummaryController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @GetMapping("/summary")
    public String showSummary(Model model, HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || Arrays.stream(gameSession.getUserGuesses()).anyMatch(Objects::isNull)) {
            return "redirect:/game/start-new-game";
        }


        model.addAttribute("totalScore", gameSession.getTotalScore());
        model.addAttribute("game", gameSession);
        model.addAttribute("apiKey", googleMapsKey);

        this.gameSessionService.saveGameSession(gameSession);
        return "summary";
    }
}
