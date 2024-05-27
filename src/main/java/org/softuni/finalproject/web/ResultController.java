package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

        this.gameService.getGameSession().nextRound();
        return this.gameService.getGameSession();
    }

    @GetMapping("/result")
    public String getResult(Model model, HttpSession session) {

        if(session.getAttribute("gameSession") == null ||
                this.gameService.getGameSession().getUserGuesses()[this.gameService.getGameSession().getRound() - 1] == null) {
            return "redirect:/game";
        }


        model.addAttribute("currentLocation", this.gameService.getCurrentLocation());
        model.addAttribute("game", this.gameService);

        return "result";
    }
}
