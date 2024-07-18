package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.LoggedUserDTO;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Map;


@Controller
public class ProfileController {

    private final UserAuthService userAuthService;
    private final UserService userService;

    public ProfileController(UserAuthService userAuthService, UserService userService) {
        this.userAuthService = userAuthService;
        this.userService = userService;
    }


    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {

        LoggedUserDTO userInformation = userAuthService.getUserInformation(authentication.getName());
        Map<LocalDateTime, Integer> bestGames = userService.getBestGames(authentication.getName());

        model.addAttribute("userInformation", userInformation);
        model.addAttribute("bestGames", bestGames);

        return "profile";
    }
}
