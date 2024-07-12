package org.softuni.finalproject.web;


import com.dropbox.core.DbxWebAuth;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(basePackageClasses = DropboxAuthController.class)
public class DropboxExceptionController {

    @RequestMapping(value = "/error/dbx-auth", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({DbxWebAuth.NotApprovedException.class})
    public String handleDbxWebAuthException(Model model) {

        model.addAttribute("errorMessage", "You have to authorise Dropbox in oder to upload an image.");
        model.addAttribute("reauthenticate", "/dropbox/auth");

        return "error";
    }



}
