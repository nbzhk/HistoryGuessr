package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class GameController {
    private static final int MAX_GAME_ROUNDS = 5;

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game")
    public String game(Model model, HttpSession session) {

        if (this.gameService.getGameSession() == null) {
            startNewGame(session);
        }

        String imageUrl = gameService.getCurrentLocation().getUrl();

        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("roundNumber", gameService.getGameSession().getRound());
        model.addAttribute("score", gameService.getGameSession().getTotalScore());

        return "game";
    }

    @PostMapping("/game/start-new-game")
    public String startNewGame(HttpSession session) {
        GameDTO newGame = this.gameService.startGame();
        session.setAttribute("gameSession", newGame);

        return "redirect:/game";
    }

    @PostMapping("/game")
    public ResponseEntity<UserGuess> getUserGuess(@RequestBody UserGuess userGuess) {


        this.gameService.setUserGuess(userGuess);
        this.gameService.calculateResult();


        return ResponseEntity.ok().body(userGuess);

    }


}
