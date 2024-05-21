package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.softuni.finalproject.service.DropboxAuthService;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;


@Controller
public class DropboxAuthController {

    private final DropboxAuthService dropboxAuthService;
    private final DropboxCredentialService dropboxCredentialService;

    public DropboxAuthController(DropboxAuthService dropboxAuthService, DropboxCredentialService dropboxCredentialService) {
        this.dropboxAuthService = dropboxAuthService;
        this.dropboxCredentialService = dropboxCredentialService;
    }

    @GetMapping("/dropbox/auth")
    public void authorizeDropbox(HttpServletRequest request, HttpServletResponse response) throws  IOException {
       this.dropboxAuthService.authoriseUrl(request, response);
    }

    @GetMapping("/dropbox-auth-finish")
    public void finishDropbox(HttpServletRequest request, HttpServletResponse response) throws DbxWebAuth.ProviderException, DbxWebAuth.NotApprovedException, DbxWebAuth.BadRequestException, DbxWebAuth.BadStateException, DbxException, DbxWebAuth.CsrfException, IOException {
        this.dropboxAuthService.setCredentials(request, response);
        this.dropboxCredentialService.setCredentialToAdmin(this.dropboxAuthService.getCredentials());
    }
}
