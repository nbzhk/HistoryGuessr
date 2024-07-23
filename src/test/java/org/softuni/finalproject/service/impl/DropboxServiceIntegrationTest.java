package org.softuni.finalproject.service.impl;

import com.dropbox.core.oauth.DbxCredential;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
import org.softuni.finalproject.model.entity.DropboxCredentialEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DropboxServiceIntegrationTest {

    private static final String RAW_IMAGE = "&raw=1";

    @Autowired
    private DropboxServiceImpl dropboxService;

    @MockBean
    private UserAuthService userAuthService;

    @MockBean
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private DropboxCredentialService credentialService;


    //In order for this test to work, you should log to Dropbox Developer Console
    // and obtain a temporary key to use with credentialDTO.setAccessToken(*your_key*)
    @Test
    public void testUploadFile_ShouldThrowDbxException_SinceTokenIsInvalid() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        DropboxCredentialDTO credentialDTO = new DropboxCredentialDTO();
        credentialDTO.setAccessToken("*your_key");
        user.setDropboxCredential(modelMapper.map(credentialDTO, DropboxCredentialEntity.class));
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userAuthService.getCurrentUsername()).thenReturn("testUser");
        when(credentialService.checkCredentialValidation(any(DbxCredential.class))).thenReturn(true);

        MultipartFile file = mock(MultipartFile.class);
        byte[] bytes = file.getBytes();
        when(file.getBytes()).thenReturn(bytes);

        when(dropboxService.uploadFile(file, "testFile.jpg")).thenReturn("https://www.dropbox.com/testFile.jpg" + RAW_IMAGE);

        String result = dropboxService.uploadFile(file, "testFile.jpg");

        assertThat(result).startsWith("https://www.dropbox.com/");
        assertThat(result).endsWith(RAW_IMAGE);
    }
}
