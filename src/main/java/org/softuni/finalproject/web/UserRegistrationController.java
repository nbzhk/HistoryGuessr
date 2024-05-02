package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.softuni.finalproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/register")
    public String register() {
        return "register";
    }

    @PostMapping("/users/register")
    public String register(UserRegistrationDTO userRegistrationDTO) {

        this.userService.registerUser(userRegistrationDTO);

        return "redirect:/users/login";
    }
}
