package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.PictureDTO;
import org.softuni.finalproject.model.entity.LocationEntity;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.service.LocationService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final LocationService locationService;
    private final ModelMapper modelMapper;

    public PictureServiceImpl(PictureRepository pictureRepository, LocationService locationService, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void savePicture(String url, String description, int year, Long locationId) {
        PictureDTO pictureDTO = new PictureDTO();
        pictureDTO.setUrl(url);
        pictureDTO.setDescription(description);
        pictureDTO.setYear(year);

        PictureEntity pictureEntity = this.modelMapper.map(pictureDTO, PictureEntity.class);

        LocationEntity locationEntity = this.locationService.getLocationById(locationId);

        pictureEntity.setLocation(locationEntity);

        this.pictureRepository.save(pictureEntity);

    }
}
