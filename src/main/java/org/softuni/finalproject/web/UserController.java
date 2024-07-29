package org.softuni.finalproject.web;

import jakarta.validation.Valid;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.softuni.finalproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegistrationDTO")
    public UserRegistrationDTO userRegistrationDTO() {
        return new UserRegistrationDTO();
    }

    @GetMapping("/users/register")
    public String register(Principal principal) {

        if (principal != null) {
            return "redirect:/";
        }

        return "register";
    }

    @PostMapping("/users/register")
    public String register(@Valid UserRegistrationDTO userRegistrationDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !this.userService.register(userRegistrationDTO)) {
            redirectAttributes.addFlashAttribute("userRegistrationDTO", userRegistrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDTO", bindingResult);
            return "redirect:/users/register";
        }

        return "redirect:/users/login";
    }

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
