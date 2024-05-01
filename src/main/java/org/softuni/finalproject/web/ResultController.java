package org.softuni.finalproject.web;

import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ResultController {

    private final GameService gameService;

    public ResultController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("/result")
    @ResponseBody
    public GameDTO result()  {
        UserGuess currentGuess = gameService.getUserGuess();
        if (currentGuess.getGuessLat() != null && currentGuess.getGuessLng() != null) {
            double guessLat = currentGuess.getGuessLat();
            double guessLng = currentGuess.getGuessLng();
            int guessYear = currentGuess.getGuessYear();


            System.out.println(guessLat + " " + guessLng + " " + guessYear);
        }

        return this.gameService.getGameSession();
    }

    @GetMapping("/result")
    public String getResult() {
        return "result";
    }
}
