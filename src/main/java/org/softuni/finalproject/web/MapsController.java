package org.softuni.finalproject.web;


import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapsController {

    private final GameService gameService;

    public MapsController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/get-user-guess")
    public ResponseEntity<UserGuessDTO> getUserGuess(@RequestBody UserGuessDTO userGuessDTO, HttpSession session) {


        this.gameService.setUserGuess(userGuessDTO,(GameSessionDTO) session.getAttribute("gameSession"));
        this.gameService.calculateRoundScore((GameSessionDTO) session.getAttribute("gameSession"));


        return ResponseEntity.ok().body(userGuessDTO);

    }

    @PostMapping("/game/get-result")
    public ResponseEntity<GameSessionDTO> result(HttpSession session)  {

        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");

        return ResponseEntity.ok().body(gameSession);
    }

    @PostMapping("/game/summary")
    public ResponseEntity<GameSessionDTO> summary(HttpSession session) {

        GameSessionDTO currentGame = (GameSessionDTO) session.getAttribute("gameSession");

        session.setAttribute("gameSession", null);

        return ResponseEntity.ok().body(currentGame);
    }


}
