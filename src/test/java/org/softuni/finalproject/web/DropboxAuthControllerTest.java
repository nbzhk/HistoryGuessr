package org.softuni.finalproject.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.service.DropboxAuthService;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DropboxAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DropboxAuthService dropboxAuthService;

    @MockBean
    private DropboxCredentialService dropboxCredentialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testAuthorizeDropbox_Success() throws Exception {
        doNothing().when(dropboxAuthService).authoriseUrl(any(HttpServletRequest.class), any(HttpServletResponse.class));

        mockMvc.perform(get("/dropbox/auth"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testAuthorizeDropbox_IOException() throws Exception {
        doThrow(new IOException("Authorization failed")).when(dropboxAuthService).authoriseUrl(any(HttpServletRequest.class), any(HttpServletResponse.class));

        mockMvc.perform(get("/dropbox/auth"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testFinishDropbox_Success() throws Exception {
        doNothing().when(dropboxAuthService).setCredentials(any(HttpServletRequest.class), any(HttpServletResponse.class));
        doNothing().when(dropboxCredentialService).setCredentialToAdmin(any());

        mockMvc.perform(get("/dropbox-auth-finish"))
                .andExpect(status().isOk());
    }
}