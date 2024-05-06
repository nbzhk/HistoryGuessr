package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SummaryController {

    private final GameService gameService;

    public SummaryController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/summary")
    public String showSummary() {

        return "summary";
    }

//    @GetMapping("/summary")
//    public ResponseEntity<String> showSummary() throws IOException {
//        ClassPathResource resource = new ClassPathResource("templates/summary.html");
//        String htmlContent = new String(Objects.requireNonNull(resource.getInputStream().readAllBytes()), StandardCharsets.UTF_8);
//
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.TEXT_HTML)
//                .body(htmlContent);
//    }

    @PostMapping("/summary")
    @ResponseBody
    public GameDTO summary() {

        System.out.println(gameService);

        return this.gameService.getGameSession();
    }

}
