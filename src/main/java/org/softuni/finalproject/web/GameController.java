package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    @Value("${google.maps.key}")
    private String googleMapsKey;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String gameView(Model model, HttpSession session) {
        GameSessionDTO gameSession = this.gameService.getCurrentGame(session);

        Boolean fromResult = (Boolean) session.getAttribute("fromResult");
        if (fromResult != null && fromResult) {
            session.removeAttribute("fromResult");
            gameSession.nextRound();
            return "redirect:/";
        }


        if (gameSession == null) {
            return "redirect:/game/start-new-game";
        }

        if (gameSession.lastRound()) {
            //TODO change last round to previous round
            gameSession.nextRound();
            return "redirect:/result";
        }

        String imageUrl = this.gameService.getCurrentLocation(gameSession, gameSession.getRound()).getUrl();

        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("roundNumber", gameSession.getRound());
        model.addAttribute("score", gameSession.getTotalScore());
        model.addAttribute("apiKey", googleMapsKey);

        return "game";
    }

    @GetMapping("/start-new-game")
    public String startNewGame(HttpSession session) {
        GameSessionDTO newGame = this.gameService.startGame(session);
        session.setAttribute("gameSession", newGame);

        return "redirect:/game";
    }
}
