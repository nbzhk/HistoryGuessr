package org.softuni.finalproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class UserLoginController {

    @GetMapping("/users/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }

        return "login";
    }

    @PostMapping("/users/login-error")
    public String loginError(
            @ModelAttribute("username") String username,
            Model model) {

        model.addAttribute("username", username);
        model.addAttribute("badCredentials", true);


        return "login";
    }
}
