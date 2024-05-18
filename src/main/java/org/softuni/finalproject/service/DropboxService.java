package org.softuni.finalproject.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DropboxService {
    void getFullAccountInfo() throws DbxException;

    String uploadFile(MultipartFile file, String fileName) throws DbxException, IOException;

    DbxClientV2 getClient();
}
