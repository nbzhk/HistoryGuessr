package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.model.entity.DailyChallengeEntity;
import org.softuni.finalproject.repository.DailyChallengeRepository;
import org.softuni.finalproject.service.DailyChallengeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyChallengeServiceImpl implements DailyChallengeService {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final ModelMapper modelMapper;

    public DailyChallengeServiceImpl(DailyChallengeRepository dailyChallengeRepository, ModelMapper modelMapper) {
        this.dailyChallengeRepository = dailyChallengeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public DailyChallengeDTO getDailyChallenge() {
        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        return this.modelMapper.map(byDate, DailyChallengeDTO.class);

    }
}
