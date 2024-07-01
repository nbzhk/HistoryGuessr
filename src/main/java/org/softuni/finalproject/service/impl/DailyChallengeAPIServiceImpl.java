package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.model.entity.DailyChallengeEntity;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.repository.DailyChallengeRepository;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyChallengeAPIServiceImpl implements DailyChallengeAPIService {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;

    public DailyChallengeAPIServiceImpl(DailyChallengeRepository dailyChallengeRepository, PictureRepository pictureRepository, ModelMapper modelMapper) {
        this.dailyChallengeRepository = dailyChallengeRepository;
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create() {
        Optional<PictureEntity> randomDailyPicture =
                this.pictureRepository.findRandomDailyPicture(1);

        if (randomDailyPicture.isPresent() && !this.dailyChallengeRepository.existsByDate(LocalDate.now())) {
            DailyChallengeEntity dailyChallenge =
                    new DailyChallengeEntity(randomDailyPicture.get(), LocalDate.now());

            this.dailyChallengeRepository.save(dailyChallenge);
        }
    }

    @Override
    public DailyChallengeDTO getCurrentChallenge() {
        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        return modelMapper.map(byDate, DailyChallengeDTO.class);
    }
}
