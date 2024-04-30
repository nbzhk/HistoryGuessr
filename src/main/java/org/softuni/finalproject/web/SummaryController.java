package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.service.GameSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class SummaryController {

    private final GameSession gameSession;

    public SummaryController(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @GetMapping("/summary")
    public ResponseEntity<String> showSummary() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/summary.html");
        String htmlContent = new String(Objects.requireNonNull(resource.getInputStream().readAllBytes()), StandardCharsets.UTF_8);


        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }

    @PostMapping("/summary")
    @ResponseBody
    public GameDTO summary() {

        System.out.println(gameSession);

        return this.gameSession.getGameSession();
    }

}
