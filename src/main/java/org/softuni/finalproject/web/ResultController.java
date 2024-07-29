package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        session.setAttribute("fromResult", true);

        model.addAttribute("currentLocation", this.gameService.getCurrentLocation(gameSession, gameSession.getRound()));
        model.addAttribute("roundNumber", gameSession.getRound());
        model.addAttribute("currentGame", gameSession);
        model.addAttribute("apiKey", googleMapsKey);


        return "result";
    }

    @GetMapping("/next-round")
    public String nextRound(HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || gameSession.getUserGuesses()[gameSession.getRound() - 1] == null) {
            return "redirect:/game/start-new-game";
        }

        gameSession.nextRound();
        session.setAttribute("fromResult", false);

        if (gameSession.lastRound()) {
            // this actually sets the round back to 5
            gameSession.nextRound();
            return "redirect:/summary";
        } else {
            return "redirect:/game";
        }
    }
}
