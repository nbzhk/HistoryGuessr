package org.softuni.finalproject.service.impl;

import com.dropbox.core.*;
import com.dropbox.core.oauth.DbxCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.service.DropboxAuthService;

import java.io.IOException;


public class DropboxAuthServiceImpl implements DropboxAuthService {

    private static final String REDIRECT_URI_AUTH_FINISH = System.getenv("BASE_URL") + "/dropbox-auth-finish";
    private static final String REDIRECT_URI_UPLOAD_PAGE = System.getenv("BASE_URL") +"/admin/upload";
    private static final String SESSION_KEY = "dropbox-auth-csrf-token";

    private final DbxRequestConfig dbxRequestConfig;
    private final String appKey;
    private final String appSecret;
    private DbxCredential credential;


    public DropboxAuthServiceImpl(DbxRequestConfig dbxRequestConfig, String appKey, String appSecret) {
        this.dbxRequestConfig = dbxRequestConfig;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    @Override
    public void authoriseUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, SESSION_KEY);

        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URI_AUTH_FINISH, csrfTokenStore)
                .withTokenAccessType(TokenAccessType.OFFLINE)
                .build();

        DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        DbxWebAuth auth = new DbxWebAuth(dbxRequestConfig, appInfo);

        String authorizePageUri = auth.authorize(authRequest);

        response.sendRedirect(authorizePageUri);

    }

    @Override
    public void setCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException, DbxWebAuth.ProviderException, DbxWebAuth.NotApprovedException, DbxWebAuth.BadRequestException, DbxWebAuth.BadStateException, DbxException, DbxWebAuth.CsrfException {
        HttpSession session = request.getSession(true);
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, SESSION_KEY);

        DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        DbxWebAuth auth = new DbxWebAuth(dbxRequestConfig, appInfo);

        DbxAuthFinish finish = auth
                .finishFromRedirect(REDIRECT_URI_AUTH_FINISH, csrfTokenStore, request.getParameterMap());

        this.credential = new DbxCredential(finish.getAccessToken(),
                finish.getExpiresAt(),
                finish.getRefreshToken(),
                this.appKey, this.appSecret);

        response.sendRedirect(REDIRECT_URI_UPLOAD_PAGE);
    }


@Override
public DbxCredential getCredentials() {
    return this.credential;
}


}
