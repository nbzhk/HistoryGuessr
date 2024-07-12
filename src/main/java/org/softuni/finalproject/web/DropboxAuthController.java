package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuth.BadStateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softuni.finalproject.model.dto.DropboxErrorInfoDTO;
import org.softuni.finalproject.service.DropboxAuthService;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class DropboxAuthController {

    private static final Logger logger = LoggerFactory.getLogger(DropboxAuthController.class);

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
        //TODO: EXCEPTION HANDLING !!!
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

    @ExceptionHandler(BadStateException.class)
    public ResponseEntity<DropboxErrorInfoDTO> handleBadStateException(BadStateException ex) {
        logger.error("Bad state exception", ex);
        DropboxErrorInfoDTO errorInfoDTO =
                new DropboxErrorInfoDTO(HttpStatus.UNAUTHORIZED.toString(), "Unexpected state");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorInfoDTO);
    }

//    @ExceptionHandler(NotApprovedException.class)
//    public ResponseEntity<DropboxErrorInfoDTO> handleNotApprovedException(NotApprovedException ex) {
//        logger.error("Not approved exception", ex);
//        DropboxErrorInfoDTO errorInfoDTO = new DropboxErrorInfoDTO(HttpStatus.UNAUTHORIZED.toString(),
//                "You should authorise dropbox, in order to upload an image.");
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorInfoDTO);
//    }


}
