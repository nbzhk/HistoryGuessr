package org.softuni.finalproject.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import org.softuni.finalproject.service.DropboxService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class DropboxServiceImpl implements DropboxService {

    private final DbxClientV2 client;

    public DropboxServiceImpl(DbxClientV2 client) {
        this.client = client;

    }

    @Override
    public void getFullAccountInfo() throws DbxException {
        FullAccount account = this.client.users().getCurrentAccount();
        System.out.println(account);
    }

    @Override
    public String uploadFile(MultipartFile file, String fileName) throws DbxException, IOException {
        InputStream stream = new ByteArrayInputStream(file.getBytes());
        client.files().uploadBuilder("/Pictures/" + fileName).uploadAndFinish(stream);

        return client.files().getMetadata("/Pictures/" + fileName).getPreviewUrl();
    }
}

