package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
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
    public GameSessionDTO result(HttpSession session)  {
        return  (GameSessionDTO) session.getAttribute("gameSession");

    }

    @GetMapping("/result")
    public String getResult(Model model, HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || gameSession.getUserGuesses()[gameSession.getRound() - 1] == null) {
            return "redirect:/game";
        }


        model.addAttribute("currentLocation", this.gameService.getCurrentLocation(gameSession));
        model.addAttribute("currentGame", gameSession);


        return "result";
    }
}
