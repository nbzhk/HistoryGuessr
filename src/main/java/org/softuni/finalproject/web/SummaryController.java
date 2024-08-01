package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameService;
import org.softuni.finalproject.service.GameSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Objects;

@Controller
public class SummaryController {

    private final GameSessionService gameSessionService;
    private final GameService gameService;
    @Value("${google.maps.key}")
    private String googleMapsKey;

    public SummaryController(GameSessionService gameSessionService, GameService gameService) {
        this.gameSessionService = gameSessionService;
        this.gameService = gameService;
    }

    @GetMapping("/summary")
    public String showSummary(Model model, HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || Arrays.stream(gameSession.getUserGuesses()).anyMatch(Objects::isNull)) {
            return "redirect:/game/start-new-game";
        }


        model.addAttribute("totalScore", gameSession.getTotalScore());
        model.addAttribute("game", gameSession);
        model.addAttribute("fromBest", false);
        model.addAttribute("apiKey", googleMapsKey);


        return "summary";
    }

    @PostMapping("/summary/save")
    public String saveGameSession(HttpSession session, @RequestParam("action") String action) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        this.gameSessionService.saveGameSession(gameSession);

        session.removeAttribute("gameSession");

        if(action.equals("newGame")) {
            return "redirect:/game/start-new-game";
        }

        return "redirect:/";
    }

    @GetMapping("/summary/round={round}")
    public String showRoundSummary(Model model, HttpSession session, @PathVariable("round") Integer round) {

        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        model.addAttribute("currentLocation", this.gameService.getCurrentLocation(gameSession, round));
        model.addAttribute("roundNumber", round);
        model.addAttribute("currentGame", gameSession);
        model.addAttribute("apiKey", googleMapsKey);

        return "result";
    }
}
