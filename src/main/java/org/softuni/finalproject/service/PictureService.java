package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.entity.PictureEntity;

import java.util.List;

public interface PictureService {

    void savePicture(String url, String description, int year, double latitude, double longitude);

    List<PictureEntity> getCurrentGamePictures(PictureLocationDTO[] pictures);

    PictureLocationDTO[] createPictureLocations();
}
