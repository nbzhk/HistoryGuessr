package org.softuni.finalproject.service.impl;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;

public class DropboxServiceImplTest {

    @Mock
    private DbxRequestConfig requestConfig;

    @Mock
    private UserAuthService userAuthService;

    @Mock
    private UserService userService;

    @Mock
    private DropboxCredentialService dropboxCredentialService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DbxClientV2 dbxClientV2;

    @InjectMocks
    private DropboxServiceImpl dropboxServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



}
