package org.softuni.finalproject.web;

import com.dropbox.core.*;
import jakarta.validation.Valid;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
import org.softuni.finalproject.model.dto.PictureDataDTO;
import org.softuni.finalproject.service.DropboxService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class ImageLocationUploadController {

    private final DropboxService dropboxService;
    private final PictureService pictureService;
    private DropboxCredentialDTO currentUserCredential;


    public ImageLocationUploadController(DropboxService dropboxService, PictureService pictureService) {
        this.dropboxService = dropboxService;
        this.pictureService = pictureService;
    }

    @ModelAttribute("pictureData")
    public PictureDataDTO pictureData() {
        return new PictureDataDTO();
    }

    @ModelAttribute("apiKey")
    public String apiKey(@Value("${google.maps.key}") String apiKey) {
        return apiKey;
    }

    @GetMapping("/upload")
    public String imageLocationUpload() throws DbxException {
        if (currentUserCredential == null) {
            currentUserCredential = this.dropboxService.getUserDropboxCredential();
            if (currentUserCredential == null) {
                return "redirect:/dropbox/auth";
            }
        }

        return "image-location-upload";
    }

    @PostMapping("/upload")
    public String imageLocationUpload(@Valid PictureDataDTO pictureData,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) throws BadRequestException, InvalidAccessTokenException, RetryException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("pictureData", pictureData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pictureData", bindingResult);

            return "redirect:/admin/upload";
        }

        MultipartFile file = pictureData.getPicture();

        try {
            String url = this.dropboxService.uploadFile(file, file.getOriginalFilename());
            this.pictureService.savePicture(url,
                    pictureData.getDescription(),
                    pictureData.getYear(),
                    pictureData.getLatitude(),
                    pictureData.getLongitude());
            redirectAttributes.addFlashAttribute("success", "Image uploaded successfully");
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getRequestId(), e.getMessage());
        } catch (InvalidAccessTokenException e) {
            throw new InvalidAccessTokenException(e.getRequestId(), e.getMessage(), e.getAuthError());
        } catch (RetryException e) {
            throw new RetryException(e.getRequestId(), e.getMessage());
        } catch (DbxException e) {
            redirectAttributes.addFlashAttribute("error", "Image upload failed");
        } catch (IOException | DbxWebAuth.NotApprovedException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/admin/upload";
    }
}
