package org.softuni.finalproject.web;

import com.dropbox.core.DbxException;
import org.softuni.finalproject.service.DropboxService;
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


    public ImageLocationUploadController(DropboxService dropboxService) {
        this.dropboxService = dropboxService;
    }

    @GetMapping("/upload")
    public String imageLocationUpload() throws DbxException {
        this.dropboxService.getFullAccountInfo();
        return "image-location-upload";
    }

    @PostMapping("/upload")
    public String imageLocationUpload(@RequestParam("file") MultipartFile file,
                                      @RequestParam("description") String description,
                                      @RequestParam("longitude") Double longitude,
                                      @RequestParam("latitude") Double latitude) throws IOException, DbxException {

        this.dropboxService.uploadFile(file, file.getOriginalFilename());

        return "redirect:/admin/upload";
    }
}
