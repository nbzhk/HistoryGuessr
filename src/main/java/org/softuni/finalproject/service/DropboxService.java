package org.softuni.finalproject.service;

import com.dropbox.core.DbxException;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DropboxService {

    String uploadFile(MultipartFile file, String fileName) throws DbxException, IOException;

    DropboxCredentialDTO getUserDropboxCredential() throws DbxException;


}
