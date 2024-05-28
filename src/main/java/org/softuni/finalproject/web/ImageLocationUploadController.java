package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
import org.softuni.finalproject.service.DropboxService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String imageLocationUpload(@RequestParam("file") MultipartFile file,
                                      @RequestParam("description") String description,
                                      @RequestParam("longitude") Double longitude,
                                      @RequestParam("latitude") Double latitude,
                                      @RequestParam("year") Integer year,
                                      RedirectAttributes redirectAttributes) {
        try {
            String url = this.dropboxService.uploadFile(file, file.getOriginalFilename());
            this.pictureService.savePicture(url, description, year, latitude, longitude);
            redirectAttributes.addFlashAttribute("success", "Image uploaded successfully");

        } catch (DbxException e) {
            redirectAttributes.addFlashAttribute("error", "Image upload failed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/admin/upload";
    }
}
