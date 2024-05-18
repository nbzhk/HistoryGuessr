package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import org.softuni.finalproject.service.DropboxService;
import org.softuni.finalproject.service.LocationService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class ImageLocationUploadController {

    private final DropboxService dropboxService;
    private final LocationService locationService;
    private final PictureService pictureService;


    public ImageLocationUploadController(DropboxService dropboxService, LocationService locationService, PictureService pictureService) {
        this.dropboxService = dropboxService;
        this.locationService = locationService;
        this.pictureService = pictureService;
    }

    @GetMapping("/upload")
    public String imageLocationUpload() throws DbxException {
        if (this.dropboxService.getClient() == null){
            return "redirect:/dropbox/auth";
        }
        this.dropboxService.getFullAccountInfo();
        return "image-location-upload";
    }

    @PostMapping("/upload")
    public String imageLocationUpload(@RequestParam("file") MultipartFile file,
                                      @RequestParam("description") String description,
                                      @RequestParam("longitude") Double longitude,
                                      @RequestParam("latitude") Double latitude,
                                      @RequestParam("year") Integer year) throws IOException, DbxException {

        String url = this.dropboxService.uploadFile(file, file.getOriginalFilename());

        Long locationId = this.locationService.saveLocation(latitude, longitude);

        this.pictureService.savePicture(url, description, year, locationId);

        return "redirect:/admin/upload";
    }
}
