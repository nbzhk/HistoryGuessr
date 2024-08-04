package org.softuni.finalproject.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.model.dto.UserInfoDTO;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ManageUsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testManageUsers() throws Exception {
        UserInfoDTO testUserOne = new UserInfoDTO();
        testUserOne.setUsername("testOne");
        testUserOne.setUserRoles(List.of(UserRoleEnum.USER, UserRoleEnum.ADMIN));
        testUserOne.setRegistrationDate(LocalDate.now());
        UserInfoDTO testUseTwo = new UserInfoDTO();
        testUseTwo.setUsername("testTwo");
        testUseTwo.setUserRoles(List.of(UserRoleEnum.USER, UserRoleEnum.ADMIN));
        testUseTwo.setRegistrationDate(LocalDate.now());
        List<UserInfoDTO> users = Arrays.asList(testUserOne, testUseTwo);

        when(this.userService.getAllUsers()).thenReturn(users);

        this.mockMvc.perform(get("/admin/manage-users"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-users"))
                .andExpect(model().attribute("users", users));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testPromoteUserToAdmin() throws Exception {
        String username = "testUser";

        this.mockMvc.perform(patch("/users/promote-admin/{username}", username)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/manage-users"));

        verify(this.userService).promoteUserToAdmin(username);
    }


    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testDemoteAdminToUser() throws Exception {
        String username = "demoteAdmin";

        this.mockMvc.perform(patch("/users/demote-admin/{username}", username)
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/manage-users"));

        verify(this.userService).demoteAdminToUser(username);
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        String username = "userToDelete";

        this.mockMvc.perform(delete("/users/delete/{username}", username)
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/manage-users"));

        verify(this.userService).delete(username);
    }


}