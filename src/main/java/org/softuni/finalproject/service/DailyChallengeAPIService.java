package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.ChallengeParticipantDTO;
import org.softuni.finalproject.model.dto.CurrentParticipantDataDTO;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;

public interface DailyChallengeAPIService {
    void create();

    DailyChallengeDTO getCurrentChallenge();

    void addParticipant(DailyChallengeDTO dailyChallengeDTO);

    void setUserGuess(ChallengeParticipantDTO challengeParticipantDTO, UserGuessDTO userGuessDTO);

    void setParticipantScore(ChallengeParticipantDTO challengeParticipantDTO, int totalScore);

    CurrentParticipantDataDTO getForCurrentUser(String username);

    double currentGuessDistance(DailyChallengeDTO dailyChallengeDTO, String username);

    ChallengeParticipantDTO userAlreadyParticipated();
}
