package org.softuni.finalproject.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface DropboxAuthService {
    void authoriseUrl(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void getAccessToken(HttpServletRequest request, HttpServletResponse response) throws DbxException, DbxWebAuth.ProviderException, DbxWebAuth.NotApprovedException, DbxWebAuth.BadRequestException, DbxWebAuth.BadStateException, DbxWebAuth.CsrfException, IOException;

    String getAccessToken();

    String getAppKey();

    Long getExpiresIn();
}
