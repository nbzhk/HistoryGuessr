package org.softuni.finalproject.web;

import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.service.GameSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
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

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }

    @PostMapping("/game")
    public ResponseEntity<Void> getUserGuess(@RequestBody UserGuess userGuess) {

        this.gameSession.setUserGuess(userGuess);
        this.gameSession.calculateResult();

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/result")
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
