package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.softuni.finalproject.service.DropboxAuthService;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class DropboxAuthController {


    private final DropboxAuthService dropboxAuthService;
    private final DropboxCredentialService dropboxCredentialService;

    public DropboxAuthController(DropboxAuthService dropboxAuthService, DropboxCredentialService dropboxCredentialService) {
        this.dropboxAuthService = dropboxAuthService;
        this.dropboxCredentialService = dropboxCredentialService;
    }

    @GetMapping("/dropbox/auth")
    public ResponseEntity<Void> authorizeDropbox(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.dropboxAuthService.authoriseUrl(request, response);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dropbox-auth-finish")
    public ResponseEntity<Void> finishDropbox(HttpServletRequest request, HttpServletResponse response)
            throws DbxWebAuth.ProviderException, DbxWebAuth.NotApprovedException,
            DbxWebAuth.BadRequestException, DbxWebAuth.BadStateException, DbxException,
            DbxWebAuth.CsrfException,
            IOException {

            this.dropboxAuthService.setCredentials(request, response);
            this.dropboxCredentialService.setCredentialToAdmin(this.dropboxAuthService.getCredentials());

            return ResponseEntity.ok().build();
    }


}
