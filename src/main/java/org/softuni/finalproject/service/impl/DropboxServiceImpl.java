package org.softuni.finalproject.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.softuni.finalproject.service.DropboxService;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class DropboxServiceImpl implements DropboxService {

    private static final String RAW_IMAGE = "&raw=1";


    private final DbxRequestConfig dbxRequestConfig;
    private final UserAuthService userAuthService;
    private final UserService userService;
    private final DropboxCredentialService credentialService;
    private final ModelMapper modelMapper;
    private DbxClientV2 client;


    public DropboxServiceImpl(DbxRequestConfig dbxRequestConfig,
                              UserAuthService userAuthService,
                              UserService userService,
                              DropboxCredentialService credentialService, ModelMapper modelMapper) {
        this.dbxRequestConfig = dbxRequestConfig;
        this.userAuthService = userAuthService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.modelMapper = modelMapper;

    }


    @Override
    public DbxCredential getUserDropboxCredential() {
        String currentUsername = this.userAuthService.getCurrentUsername();
        Optional<UserEntity> user = this.userService.findByUsername(currentUsername);


        if (user.isPresent()) {
            UserEntity userEntity = user.get();

            if (userEntity.getDropboxCredential() != null) {
                return this.modelMapper.map(userEntity.getDropboxCredential(), DbxCredential.class);
            }
        }

        return null;
    }


    //Uploads the picture to Dropbox and saves the raw url in the database
    @Override
    public String uploadFile(MultipartFile file, String fileName) throws DbxException, IOException {
        if (this.client == null) {
            this.client = initializeClient();
        }

        InputStream stream = new ByteArrayInputStream(file.getBytes());
        //TODO: Check if image exists
        client.files().uploadBuilder("/Pictures/" + fileName)
                .uploadAndFinish(stream);

        SharedLinkMetadata sharedLinkMetadata = client.sharing()
                .createSharedLinkWithSettings("/Pictures/" + fileName);

        return sharedLinkMetadata.getUrl() + RAW_IMAGE;
    }


    private DbxClientV2 initializeClient() throws DbxException {
        DbxCredential credential = getUserDropboxCredential();
        boolean tokenIsValid = this.credentialService.checkCredentialValidation(credential);

        if (!tokenIsValid) {
            credential = getUserDropboxCredential();
        }

        return this.client = new DbxClientV2(dbxRequestConfig, credential);
    }

}

