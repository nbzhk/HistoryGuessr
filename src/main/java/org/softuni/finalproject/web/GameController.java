package org.softuni.finalproject.web;

import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.service.GameSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class GameController {

    private final GameSession gameSession;


    public GameController(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @GetMapping("/game")
    public ResponseEntity<String> showGameWindow() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/game.html");
        String htmlContent = new String(Objects.requireNonNull(resource.getInputStream().readAllBytes()), StandardCharsets.UTF_8);

        String imageUrl = this.gameSession.getCurrentLocation().getImgUrl();
        htmlContent = htmlContent.replace("<img src=\"\" alt=\"image\">", "<img src=\"" + imageUrl + "\" alt=\"image\"/>");

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }


    @PostMapping("/game")
    public ResponseEntity<UserGuess> getUserGuess(@RequestBody UserGuess userGuess) {



        this.gameSession.setUserGuess(userGuess);
        this.gameSession.calculateResult();


        return ResponseEntity.ok().body(userGuess);


    }



}
