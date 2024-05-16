package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.UserInfoDTO;
import org.softuni.finalproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ManageUsersController {
    private final UserService userService;

    public ManageUsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/manage-users")
    public String manageUsers(Model model) {
        List<UserInfoDTO> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        return "manage-users";
    }

    @PatchMapping("/users/make-admin/{username}")
    public String updateUser(@ModelAttribute("user") UserInfoDTO user,
                             @PathVariable String username) {


        return "redirect:/admin/manage-users";
    }

    @DeleteMapping("/users/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) {

        this.userService.deleteUser(username);
        return "redirect:/admin/manage-users";
    }
}
