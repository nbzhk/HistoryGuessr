package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.*;
import org.softuni.finalproject.model.entity.ChallengeParticipantEntity;
import org.softuni.finalproject.model.entity.DailyChallengeEntity;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.DailyChallengeRepository;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.softuni.finalproject.service.GameService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class DailyChallengeAPIServiceImpl implements DailyChallengeAPIService {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final GameService gameService;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public DailyChallengeAPIServiceImpl(DailyChallengeRepository dailyChallengeRepository, GameService gameService, PictureRepository pictureRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.dailyChallengeRepository = dailyChallengeRepository;
        this.gameService = gameService;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
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

    @Override
    public ChallengeParticipantDTO addParticipant(DailyChallengeDTO dailyChallengeDTO) {
        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        if (dailyChallengeDTO.getParticipants() == null || dailyChallengeDTO.getParticipants().isEmpty()) {
            dailyChallengeDTO.setParticipants(new ArrayList<>());
        }
        ChallengeParticipantDTO challengeParticipantDTO = new ChallengeParticipantDTO();

        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        if (principal != null) {
            Optional<UserEntity> user = this.userRepository.findByUsername(principal.getName());

            if (user.isPresent()) {

                challengeParticipantDTO.setUsername(user.get().getUsername());

                dailyChallengeDTO.getParticipants().add(challengeParticipantDTO);

                ChallengeParticipantEntity participant =
                        this.modelMapper.map(challengeParticipantDTO, ChallengeParticipantEntity.class);

                participant.setUser(user.get());
                participant.setDailyChallenge(byDate);

                //TODO check if participant already exists

                byDate.getParticipants().add(participant);
            }


            this.dailyChallengeRepository.save(byDate);
        }

        return challengeParticipantDTO;

    }

    @Override
    public void setUserGuess(ChallengeParticipantDTO challengeParticipantDTO, UserGuessDTO userGuessDTO) {
        Optional<UserEntity> user = this.userRepository.findByUsername(challengeParticipantDTO.getUsername());
        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        if (user.isPresent() && byDate != null) {
            Optional<ChallengeParticipantEntity> participant = byDate.getParticipants()
                    .stream()
                    .filter(p -> p.getUser().getUsername().equals(user.get().getUsername()))
                    .findFirst();

            if (participant.isPresent()) {
                byDate.getParticipants().remove(participant.get());

                participant.get().setDailyChallenge(byDate);
                participant.get().setUser(user.get());
                participant.get().setGuess(userGuessDTO);

                byDate.getParticipants().add(participant.get());

                this.dailyChallengeRepository.save(byDate);
            }
        }
    }

    @Override
    public void setParticipantScore(ChallengeParticipantDTO challengeParticipantDTO, int totalScore) {
        Optional<UserEntity> user = this.userRepository.findByUsername(challengeParticipantDTO.getUsername());
        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        if (user.isPresent() && byDate != null) {
            Optional<ChallengeParticipantEntity> participant = byDate.getParticipants()
                    .stream()
                    .filter(p -> p.getUser().getUsername().equals(user.get().getUsername()))
                    .findFirst();

            if (participant.isPresent()) {
                byDate.getParticipants().remove(participant.get());

                participant.get().setDailyChallenge(byDate);
                participant.get().setUser(user.get());
                participant.get().setScore(totalScore);

                byDate.getParticipants().add(participant.get());

                this.dailyChallengeRepository.save(byDate);
            }
        }
    }

    @Override
    public CurrentParticipantDataDTO getForCurrentUser(String username) {

        Optional<UserEntity> byUsername = this.userRepository.findByUsername(username);
        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        CurrentParticipantDataDTO currentParticipantDataDTO = new CurrentParticipantDataDTO();

        Optional<ChallengeParticipantEntity> participant = byDate.getParticipants()
                .stream()
                .filter(p -> p.getUser().getUsername().equals(byUsername.get().getUsername()))
                .findFirst();


        if (byUsername.isPresent()) {
            currentParticipantDataDTO.setUsername(byUsername.get().getUsername());
            currentParticipantDataDTO.setPicture(modelMapper.map(byDate.getPicture(), PictureLocationDTO.class));
            currentParticipantDataDTO.setUserGuessDTO(participant.get().getGuess());
            currentParticipantDataDTO.setScore(participant.get().getScore());

        }

        return currentParticipantDataDTO;
    }

    @Override
    public double currentGuessDistance(DailyChallengeDTO dailyChallengeDTO, String username) {

        Optional<UserEntity> byUsername = this.userRepository.findByUsername(username);
        DailyChallengeEntity byDate = this.dailyChallengeRepository.findByDate(LocalDate.now());

        Optional<ChallengeParticipantEntity> participant = byDate.getParticipants()
                .stream()
                .filter(p -> p.getUser().getUsername().equals(byUsername.get().getUsername()))
                .findFirst();

        return this.gameService.getDailyGuessDistance(dailyChallengeDTO, participant.get().getGuess());

    }
}
