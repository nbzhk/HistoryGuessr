package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.CurrentParticipantDataDTO;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;

public interface DailyChallengeService {
    DailyChallengeDTO getDailyChallenge();

    void setParticipant(DailyChallengeDTO dailyChallengeDTO);

    CurrentParticipantDataDTO getCurrentChallengerData(String username);

    boolean userAlreadyParticipated();
}
