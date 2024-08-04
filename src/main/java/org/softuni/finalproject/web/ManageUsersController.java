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

    @PatchMapping("/users/promote-admin/{username}")
    public String updateUser(@PathVariable String username) {

        this.userService.promoteUserToAdmin(username);

        return "redirect:/admin/manage-users";
    }

    @PatchMapping("/users/demote-admin/{username}")
    public String demoteAdmin(@PathVariable String username) {

        this.userService.demoteAdminToUser(username);

        return "redirect:/admin/manage-users";
    }

    @DeleteMapping("/users/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) {

        this.userService.delete(username);
        return "redirect:/admin/manage-users";
    }
}
