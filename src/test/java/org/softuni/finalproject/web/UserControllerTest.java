package org.softuni.finalproject.web;

import org.junit.jupiter.api.Test;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testRegistrationGet_WhenPrincipalIsNotNull_ShouldReturnHomeView() throws Exception {
        mockMvc.perform(get("/users/register")
                        .with(user("user")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testRegistrationGet_WhenPrincipalIsNull_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testLoginGet_WhenPrincipalIsNull_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLogin_WhenPrincipalIsNotNull_ShouldReturnHome() throws Exception {
        mockMvc.perform(get("/users/login")
                        .with(user("user")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testRegistration_OnSuccess() throws Exception {
        mockMvc.perform(post("/users/register")
                .param("username", "test")
                .param("email", "test@test")
                .param("password", "testPass")
                .param("confirmPassword", "testPass")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }


    @Test
    void testRegistration_OnFailure() throws Exception {
        UserRegistrationDTO invalidUser = new UserRegistrationDTO();

        invalidUser.setUsername("");
        invalidUser.setEmail("");
        invalidUser.setPassword("kurz");
        invalidUser.setConfirmPassword("kurzer");

        this.mockMvc.perform(post("/users/register")
                .param("username", invalidUser.getUsername())
                .param("email", invalidUser.getEmail())
                .param("password", invalidUser.getPassword())
                .param("confirmPassword", invalidUser.getConfirmPassword())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeExists("userRegistrationDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userRegistrationDTO"));
    }

    @Test
    void testLoginError() throws Exception {
        mockMvc.perform(post("/users/login-error")
                        .param("username", "test")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("badCredentials"))
                .andExpect(model().attribute("badCredentials", true));
    }

}