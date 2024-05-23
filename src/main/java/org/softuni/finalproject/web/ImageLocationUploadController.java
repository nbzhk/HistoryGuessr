package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import org.softuni.finalproject.model.dto.DropboxCredentialDTO;
import org.softuni.finalproject.service.DropboxService;
import org.softuni.finalproject.service.LocationService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class ImageLocationUploadController {

    private final DropboxService dropboxService;
    private final LocationService locationService;
    private final PictureService pictureService;
    private DropboxCredentialDTO currentUserCredential;


    public ImageLocationUploadController(DropboxService dropboxService, LocationService locationService, PictureService pictureService) {
        this.dropboxService = dropboxService;
        this.locationService = locationService;
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
            Long locationId = this.locationService.saveLocation(latitude, longitude);
            this.pictureService.savePicture(url, description, year, locationId);
            redirectAttributes.addFlashAttribute("success", "Image uploaded successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Image upload failed");
        }

        return "redirect:/admin/upload";
    }
}
