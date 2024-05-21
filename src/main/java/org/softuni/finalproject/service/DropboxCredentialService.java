package org.softuni.finalproject.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.oauth.DbxCredential;

public interface DropboxCredentialService {

    void setCredentialToAdmin(DbxCredential credential);

    boolean checkCredentialValidation(DbxCredential credential) throws DbxException;


}
