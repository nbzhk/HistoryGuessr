package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
import org.softuni.finalproject.model.entity.DropboxCredentialEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class DropboxServiceImplTest {


    @Mock
    private UserAuthService userAuthService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private DropboxServiceImpl dropboxServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserDropboxCredential_UserExists() {
        String username = "testUsername";

        UserEntity user = new UserEntity();
        user.setUsername(username);

        DropboxCredentialEntity dropboxCredential = new DropboxCredentialEntity();
        dropboxCredential.setAccessToken("testToken");
        dropboxCredential.setRefreshToken("testRefreshToken");
        dropboxCredential.setExpiresAt(1L);

        user.setDropboxCredential(dropboxCredential);

        DropboxCredentialDTO credentialDTO = new DropboxCredentialDTO();
        credentialDTO.setAccessToken("testToken");
        credentialDTO.setRefreshToken("testRefreshToken");
        credentialDTO.setExpiresAt(1L);

        when(this.userAuthService.getCurrentUsername()).thenReturn(username);
        when(this.userService.findByUsername(username)).thenReturn(Optional.of(user));

        assertNotNull(user);
        assertNotNull(user.getDropboxCredential());

        when(modelMapper.map(dropboxCredential, DropboxCredentialDTO.class)).thenReturn(credentialDTO);

        DropboxCredentialDTO returnedCredential = this.dropboxServiceImpl.getUserDropboxCredential();

        assert (returnedCredential.getAccessToken().equals(credentialDTO.getAccessToken()));
        assert (returnedCredential.getRefreshToken().equals(credentialDTO.getRefreshToken()));
        assert (returnedCredential.getExpiresAt().equals(credentialDTO.getExpiresAt()));

    }

    @Test
    void testGetUserDropboxCredential_UserDoesNotExist() {
        String username = "nonExistingUser";

        when(this.userAuthService.getCurrentUsername()).thenReturn(username);
        when(this.userService.findByUsername(username)).thenReturn(Optional.empty());

        DropboxCredentialDTO returnedCredential = this.dropboxServiceImpl.getUserDropboxCredential();

        assertNull(returnedCredential);

    }

}
