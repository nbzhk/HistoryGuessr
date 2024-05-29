package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.service.PictureService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private static final Integer MAX_ROUNDS = 5;

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void savePicture(String url, String description, int year, double latitude, double longitude) {
        PictureLocationDTO pictureLocationDTO = new PictureLocationDTO();
        pictureLocationDTO.setUrl(url);
        pictureLocationDTO.setDescription(description);
        pictureLocationDTO.setYear(year);
        pictureLocationDTO.setLatitude(latitude);
        pictureLocationDTO.setLongitude(longitude);

        PictureEntity pictureEntity = this.modelMapper.map(pictureLocationDTO, PictureEntity.class);

        this.pictureRepository.save(pictureEntity);

    }


    private PictureEntity getPictureByUrl(String url) {
        return this.pictureRepository.findByUrl(url);
    }

    @Override
    public List<PictureEntity> getCurrentGamePictures(PictureLocationDTO[] pictures) {
        return Arrays.stream(pictures)
                .map(pic -> getPictureByUrl(pic.getUrl()))
                .toList();
    }

    @Override
    public PictureLocationDTO[] createPictureLocations() {
        List<PictureEntity> randomPictures = this.pictureRepository.findRandomPictures(MAX_ROUNDS);

        return randomPictures.stream()
                .map(this::map)
                .toArray(PictureLocationDTO[]::new);

    }

    private PictureLocationDTO map(PictureEntity pictureEntity) {
        return this.modelMapper.map(pictureEntity, PictureLocationDTO.class);
    }

}
