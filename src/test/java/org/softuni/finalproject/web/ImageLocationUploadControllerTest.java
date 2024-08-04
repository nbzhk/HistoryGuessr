package org.softuni.finalproject.web;

import com.dropbox.core.BadRequestException;
import com.dropbox.core.InvalidAccessTokenException;
import com.dropbox.core.v2.auth.AuthError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
import org.softuni.finalproject.service.DropboxService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ImageLocationUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DropboxService dropboxService;

    @MockBean
    private PictureService pictureService;

    private DropboxCredentialDTO dropboxCredentialDTO;

    @BeforeEach
    void setUp() {
        dropboxCredentialDTO = new DropboxCredentialDTO();

    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testUpload_WithoutCredential_ShouldRedirectToDropboxAuth() throws Exception {
        when(this.dropboxService.getUserDropboxCredential()).thenReturn(null);

        this.mockMvc.perform(get("/admin/upload"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dropbox/auth"));
    }


    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testGetUpload_withCredential_shouldReturnUploadView() throws Exception {
        when(dropboxService.getUserDropboxCredential()).thenReturn(this.dropboxCredentialDTO);

        mockMvc.perform(get("/admin/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("image-location-upload"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testPostUpload_WithSuccess_ShouldReturnUploadView() throws Exception {
        MockMultipartFile file = new MockMultipartFile("picture", "image.jpg", "image/jpeg", "image".getBytes());

        when(this.dropboxService.uploadFile(any(MultipartFile.class), anyString())).thenReturn("http://dropbox.url");
        doNothing().when(this.pictureService).savePicture(anyString(), anyString(), anyInt(), anyDouble(), anyDouble());

        mockMvc.perform(multipart("/admin/upload")
                        .file("picture", file.getBytes())
                        .param("description", "Test description")
                        .param("year", "2024")
                        .param("latitude", "0")
                        .param("longitude", "0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/upload"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testPostUpload_WithErrors_ShouldRedirectWithErrors() throws Exception {
        MockMultipartFile file = new MockMultipartFile("picture", "image.jpg", "image/jpeg", "image".getBytes());

        mockMvc.perform(multipart("/admin/upload")
                        .file("picture", file.getBytes())
                        .param("description", "")  // Description should not be empty
                        .param("year", "0")// Year can't be 0
                        .param("latitude", "0")
                        .param("longitude", "0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/upload"))
                .andExpect(flash().attributeExists("pictureData"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.pictureData"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testPostUpload_BadRequestException_ShouldThrowBadRequestException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("picture", "image.jpg", "image/jpeg", "image".getBytes());

        when(this.dropboxService.uploadFile(any(MultipartFile.class), anyString()))
                .thenThrow(new BadRequestException("request-id", "Bad request"));

        mockMvc.perform(multipart("/admin/upload")
                        .file("picture", file.getBytes())
                        .param("description", "Valid description")
                        .param("year", "2024")
                        .param("latitude", "0")
                        .param("longitude", "0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/upload"));
    }

    @Test
    @WithMockUser(username = "test", password = "testPass", roles = {"ADMIN"})
    void testPostUpload_InvalidAccessTokenException_ShouldThrowInvalidAccessTokenException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("picture", "image.jpg", "image/jpeg", "image".getBytes());

        AuthError mockAuthError = mock(AuthError.class);

        when(this.dropboxService.uploadFile(any(MultipartFile.class), anyString()))
                .thenThrow(new InvalidAccessTokenException("request-id", "Invalid token", mockAuthError));

        mockMvc.perform(multipart("/admin/upload")
                        .file("picture", file.getBytes())
                        .param("description", "Valid description")
                        .param("year", "2024")
                        .param("latitude", "0")
                        .param("longitude", "0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/upload"));
    }
}