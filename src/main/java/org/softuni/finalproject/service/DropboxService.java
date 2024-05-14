package org.softuni.finalproject.service;

import com.dropbox.core.DbxException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DropboxService {
    void getFullAccountInfo() throws DbxException;

    String uploadFile(MultipartFile file, String fileName) throws DbxException, IOException;
}