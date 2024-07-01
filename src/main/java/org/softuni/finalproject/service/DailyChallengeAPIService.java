package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.DailyChallengeDTO;

public interface DailyChallengeAPIService {
    void create();

    DailyChallengeDTO getCurrentChallenge();
}
