package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.service.GameService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game")
    public String game(Model model) {

        String imageUrl = gameService.getCurrentLocation().getImgUrl();

        model.addAttribute("imageUrl", imageUrl);

        return "game";
    }

//    @GetMapping("/game")
//    public ResponseEntity<String> showGameWindow(CsrfToken csrfToken) throws IOException {
//
//
//        ClassPathResource resource = new ClassPathResource("templates/game.html");
//        String htmlContent = new String(Objects.requireNonNull(resource.getInputStream().readAllBytes()), StandardCharsets.UTF_8);
//
//        String imageUrl = this.gameService.getCurrentLocation().getImgUrl();
//        htmlContent = htmlContent.replace("<img src=\"\" alt=\"image\">", "<img src=\"" + imageUrl + "\" alt=\"image\"/>");
//        htmlContent = htmlContent.replace("<meta name=\"_csrf\" content=\"${_csrf.token}\"/>",
//                "<meta name=\"_csrf\" content=" + csrfToken.getToken() + ">");
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.TEXT_HTML)
//                .body(htmlContent);
//    }


    @PostMapping("/game")
    public ResponseEntity<UserGuess> getUserGuess(@RequestBody UserGuess userGuess) {


        this.gameService.setUserGuess(userGuess);
        this.gameService.calculateResult();


        return ResponseEntity.ok().body(userGuess);

    }


}
