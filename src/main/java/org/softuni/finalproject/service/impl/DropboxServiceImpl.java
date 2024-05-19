package org.softuni.finalproject.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.softuni.finalproject.model.entity.DropboxTokenEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.service.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class DropboxServiceImpl implements DropboxService {

    private static final String RAW_IMAGE = "&raw=1";

    private final DropboxAuthService dropboxAuthService;
    private final DropboxTokenService dropboxTokenService;
    private final UserAuthService authService;
    private final UserService userService;
    private DbxClientV2 client;


    public DropboxServiceImpl(DropboxAuthService dropboxAuthService, DropboxTokenService dropboxTokenService, UserAuthService authService, UserService userService) {
        this.dropboxAuthService = dropboxAuthService;
        this.dropboxTokenService = dropboxTokenService;
        this.authService = authService;
        this.userService = userService;
        this.client = initializeClient();
    }


    @Override
    public String getToken() {
        String currentUsername = this.authService.getCurrentUsername();
        Optional<UserEntity> user = this.userService.findByUsername(currentUsername);

        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            DropboxTokenEntity dropboxToken = userEntity.getDropboxToken();

            if (dropboxToken != null &&
                    dropboxToken.getToken() != null &&
                    dropboxToken.getCreationTime() != null &&
                    !tokenIsExpired(dropboxToken)) {

                return dropboxToken.getToken();
            } else {
                String accessToken = this.dropboxAuthService.getAccessToken();
                if (accessToken != null) {
                    Long expiresIn = this.dropboxAuthService.getExpiresIn();
                    this.dropboxTokenService.setAccessTokenToAdmin(accessToken, expiresIn);
                    return accessToken;
                }
            }
        }
        return null;
    }

    private boolean tokenIsExpired(DropboxTokenEntity dropboxToken) {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        long expirationTimeInSeconds = dropboxToken.getCreationTime()
                .plusSeconds(dropboxToken.getExpiresIn())
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
        return expirationTimeInSeconds < currentTimeInSeconds;
    }

    private DbxClientV2 initializeClient() {

        String accessToken = this.dropboxAuthService.getAccessToken();
        String appKey = this.dropboxAuthService.getAppKey();
        DbxRequestConfig config = DbxRequestConfig.newBuilder(appKey).build();

        if (accessToken == null) {
            return this.client = null;
        }
        return this.client = new DbxClientV2(config, accessToken);
    }


    //Uploads the picture to Dropbox and saves the raw url in the database
    @Override
    public String uploadFile(MultipartFile file, String fileName) throws DbxException, IOException {
        InputStream stream = new ByteArrayInputStream(file.getBytes());
        //TODO: Check if image exists
        client.files().uploadBuilder("/Pictures/" + fileName)
                .uploadAndFinish(stream);

        SharedLinkMetadata sharedLinkMetadata = client.sharing()
                .createSharedLinkWithSettings("/Pictures/" + fileName);

        return sharedLinkMetadata.getUrl() + RAW_IMAGE;
    }
}

