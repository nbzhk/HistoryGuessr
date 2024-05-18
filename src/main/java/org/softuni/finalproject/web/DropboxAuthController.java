package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.softuni.finalproject.service.impl.DropboxAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;


@Controller
public class DropboxAuthController {

    private final DropboxAuthService dropboxAuthService;

    public DropboxAuthController(DropboxAuthService dropboxAuthService) {
        this.dropboxAuthService = dropboxAuthService;
    }

    @GetMapping("/dropbox/auth")
    public void authorizeDropbox(HttpServletRequest request, HttpServletResponse response) throws  IOException {
       this.dropboxAuthService.authoriseUrl(request, response);

    }

    @GetMapping("/dropbox-auth-finish")
    public void finishDropbox(HttpServletRequest request, HttpServletResponse response) throws DbxWebAuth.ProviderException, DbxWebAuth.NotApprovedException, DbxWebAuth.BadRequestException, DbxWebAuth.BadStateException, DbxException, DbxWebAuth.CsrfException, IOException {
        this.dropboxAuthService.getAccessToken(request, response);
    }
}
