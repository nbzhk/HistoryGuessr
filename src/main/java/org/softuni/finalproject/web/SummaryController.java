package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Objects;

@Controller
public class SummaryController {

    private final GameSessionService gameSessionService;

    public SummaryController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @GetMapping("/summary")
    public String showSummary(Model model, HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || Arrays.stream(gameSession.getUserGuesses()).anyMatch(Objects::isNull)) {
            return "redirect:/game";
        }


        model.addAttribute("totalScore", gameSession.getTotalScore());
        model.addAttribute("game", gameSession);

        this.gameSessionService.saveGameSession(gameSession);
        return "summary";
    }

    @PostMapping("/summary")
    @ResponseBody
    public GameSessionDTO summary(HttpSession session) {

        GameSessionDTO currentGame = (GameSessionDTO) session.getAttribute("gameSession");

        session.setAttribute("gameSession", null);

        return currentGame;
    }

}
