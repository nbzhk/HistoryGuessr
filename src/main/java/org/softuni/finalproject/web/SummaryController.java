package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameDTO;
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

        if(session.getAttribute("gameSession") == null
        || Arrays.stream(this.gameService.getGameSession().getUserGuesses()).anyMatch(Objects::isNull)) {
            return "redirect:/game";
        }


        model.addAttribute("totalScore", this.gameService.getGameSession().getTotalScore());
        model.addAttribute("game", this.gameService.getGameSession());

        this.gameService.saveSession(this.gameService.getGameSession());
        session.setAttribute("gameSession", null);
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
