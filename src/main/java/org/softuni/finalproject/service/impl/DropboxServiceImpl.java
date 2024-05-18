package org.softuni.finalproject.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.users.FullAccount;
import org.softuni.finalproject.service.DropboxService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DropboxServiceImpl implements DropboxService {

    private static final String RAW_IMAGE = "&raw=1";

    private final DropboxAuthService dropboxAuthService;
    private DbxClientV2 client;
    

    public DropboxServiceImpl(DropboxAuthService dropboxAuthService) {
        this.dropboxAuthService = dropboxAuthService;
    }

    public DbxClientV2 getClient() {
        if (client == null) {
            this.dbxClientV2();
        }
        return this.client;
    }

    private void dbxClientV2() {
        String accessToken = this.dropboxAuthService.getAccessToken();
        if (accessToken == null) {
            return;
        }
        String appKey = this.dropboxAuthService.getAppKey();
        DbxRequestConfig config = DbxRequestConfig.newBuilder(appKey).build();

        this.client =  new DbxClientV2(config, accessToken);
    }

    @Override
    public void getFullAccountInfo() throws DbxException {
        FullAccount account = this.client.users().getCurrentAccount();
        System.out.println(account);
    }

    @Override
    public String uploadFile(MultipartFile file, String fileName) throws DbxException, IOException {
        InputStream stream = new ByteArrayInputStream(file.getBytes());
        //TODO: Check if image exists
        client.files().uploadBuilder("/Pictures/" + fileName)
                .uploadAndFinish(stream);

        SharedLinkMetadata sharedLinkMetadata = client.sharing()
                .createSharedLinkWithSettings("/Pictures/" + fileName );

        return sharedLinkMetadata.getUrl() + RAW_IMAGE;
    }
}

