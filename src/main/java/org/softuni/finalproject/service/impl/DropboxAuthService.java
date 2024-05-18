package org.softuni.finalproject.service.impl;

import com.dropbox.core.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class DropboxAuthService {

    private static final String REDIRECT_URI_AUTH_FINISH = "http://localhost:8080/dropbox-auth-finish";
    private static final String REDIRECT_URI_UPLOAD_PAGE = "http://localhost:8080/admin/upload";
    private static final String SESSION_KEY = "dropbox-auth-csrf-token";


    private final DbxRequestConfig dbxRequestConfig;
    private final String appKey;
    private final String appSecret;
    private String accessToken;


    public DropboxAuthService(DbxRequestConfig dbxRequestConfig, String appKey, String appSecret) {
        this.dbxRequestConfig = dbxRequestConfig;
        this.appKey = appKey;
        this.appSecret = appSecret;

    }

    public void authoriseUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, SESSION_KEY);

        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URI_AUTH_FINISH, csrfTokenStore)
                .build();

        DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        DbxWebAuth auth = new DbxWebAuth(dbxRequestConfig, appInfo);

        String authorizePageUri = auth.authorize(authRequest);

        response.sendRedirect(authorizePageUri);


    }

    public void getAccessToken(HttpServletRequest request, HttpServletResponse response) throws DbxException, DbxWebAuth.ProviderException, DbxWebAuth.NotApprovedException, DbxWebAuth.BadRequestException, DbxWebAuth.BadStateException, DbxWebAuth.CsrfException, IOException {
        HttpSession session = request.getSession(true);
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, SESSION_KEY);

        DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        DbxWebAuth auth = new DbxWebAuth(dbxRequestConfig, appInfo);

        DbxAuthFinish finish = auth.finishFromRedirect(REDIRECT_URI_AUTH_FINISH, csrfTokenStore, request.getParameterMap());

        setAccessToken(finish.getAccessToken());

        response.sendRedirect(REDIRECT_URI_UPLOAD_PAGE);
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getAppKey(){
        return this.appKey;
    }
}
