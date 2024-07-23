package org.softuni.finalproject.service.impl;

import com.dropbox.core.BadRequestException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
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
    public DropboxCredentialDTO getUserDropboxCredential() {
        String currentUsername = this.userAuthService.getCurrentUsername();
        Optional<UserEntity> user = this.userService.findByUsername(currentUsername);


        if (user.isPresent()) {
            UserEntity userEntity = user.get();

            if (userEntity.getDropboxCredential() != null) {
                return this.modelMapper.map(userEntity.getDropboxCredential(), DropboxCredentialDTO.class);
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

        try (InputStream stream = new ByteArrayInputStream(file.getBytes())) {
            client.files().uploadBuilder("/Pictures/" + fileName)
                    .uploadAndFinish(stream);

            SharedLinkMetadata sharedLinkMetadata = client.sharing()
                    .createSharedLinkWithSettings("/Pictures/" + fileName);
            return sharedLinkMetadata.getUrl() + RAW_IMAGE;
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getRequestId(), e.getMessage());
        } catch (DbxException e) {
            throw new DbxException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    private DbxClientV2 initializeClient() throws DbxException {
        DbxCredential credential = this.modelMapper.map(getUserDropboxCredential(), DbxCredential.class);
        boolean tokenIsValid = this.credentialService.checkCredentialValidation(credential);

        if (credential.getAccessToken() == null || credential.getAccessToken().isEmpty()) {
            throw new DbxException("Invalid credential");
        }

        if (!tokenIsValid) {
            credential = this.modelMapper.map(getUserDropboxCredential(), DbxCredential.class);
        }

        return this.client = new DbxClientV2(dbxRequestConfig, credential);
    }

}

