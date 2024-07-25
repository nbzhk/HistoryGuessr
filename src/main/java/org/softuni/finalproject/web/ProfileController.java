package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.LoggedUserDTO;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class ProfileController {

    private final UserAuthService userAuthService;
    private final UserService userService;
    @Value("${google.maps.key}")
    private String googleMapsKey;

    public ProfileController(UserAuthService userAuthService, UserService userService) {
        this.userAuthService = userAuthService;
        this.userService = userService;
    }

    @ModelAttribute("bestGames")
    public Map<GameSessionDTO, Integer> getBestGames(Authentication authentication) {
        return this.userService.getBestGames(authentication.getName());
    }


    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {

        LoggedUserDTO userInformation = userAuthService.getUserInformation(authentication.getName());

        model.addAttribute("userInformation", userInformation);


        return "profile";
    }

    @GetMapping("/profile/best-game/summary/{position}")
    public String bestGame(Model model, @PathVariable int position, Authentication authentication, HttpSession session) {

        Map<GameSessionDTO, Integer> bestGames = this.userService.getBestGames(authentication.getName());
        List<Map.Entry<GameSessionDTO, Integer>> entries = new ArrayList<>(bestGames.entrySet());
        Map.Entry<GameSessionDTO, Integer> currentGame = entries.get(position - 1);

        session.setAttribute("bestGame", currentGame.getKey());

        model.addAttribute("game", currentGame.getKey());
        model.addAttribute("totalScore", currentGame.getValue());
        model.addAttribute("apiKey", this.googleMapsKey);
        model.addAttribute("fromBest", true);

        return "summary";
    }
}
