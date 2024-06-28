package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ResultController {

    private final GameService gameService;
    @Value("${google.maps.key}")
    private String googleMapsKey;

    public ResultController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/result")
    public String getResult(Model model, HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || gameSession.getUserGuesses()[gameSession.getRound() - 1] == null) {
            return "redirect:/game/start-new-game";
        }

        model.addAttribute("currentLocation", this.gameService.getCurrentLocation(gameSession));
        model.addAttribute("currentGame", gameSession);
        model.addAttribute("apiKey", googleMapsKey);



        return "result";
    }

    @PostMapping("/next-round")
    public String nextRound(HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || gameSession.getUserGuesses()[gameSession.getRound() - 1] == null) {
            return "redirect:/game/start-new-game";
        }

        gameSession.nextRound();

        if (gameSession.lastRound()) {
            // this actually sets the round to 5
            gameSession.nextRound();
            return "redirect:/summary";
        } else {
            return "redirect:/game";
        }
    }

}
