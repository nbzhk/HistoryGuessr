package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Objects;

@Controller
public class SummaryController {

    private final GameService gameService;

    public SummaryController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/summary")
    public String showSummary(Model model, HttpSession session) {
        GameSessionDTO gameSession = (GameSessionDTO) session.getAttribute("gameSession");
        if(gameSession == null || Arrays.stream(gameSession.getUserGuesses()).anyMatch(Objects::isNull)) {
            return "redirect:/game";
        }


        model.addAttribute("totalScore", gameSession.getTotalScore());
        model.addAttribute("game", gameSession);

        this.gameService.saveSession(gameSession);
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
    public GameSessionDTO summary(HttpSession session) {

        GameSessionDTO currentGame = (GameSessionDTO) session.getAttribute("gameSession");

        session.setAttribute("gameSession", null);

        return currentGame;
    }

}
