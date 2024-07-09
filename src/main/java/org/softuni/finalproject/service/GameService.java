package org.softuni.finalproject.service;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.model.dto.GameSessionDTO;

public interface GameService {

    GameSessionDTO startGame(HttpSession session);

    void setUserGuess(UserGuessDTO userGuessDTO, GameSessionDTO gameSessionDTO);

    void calculateRoundScore(GameSessionDTO gameSessionDTO);

    PictureLocationDTO getCurrentLocation(GameSessionDTO gameSessionDTO);

    GameSessionDTO getCurrentGame(HttpSession session);

    void calculateDailyScore(DailyChallengeDTO dailyChallengeDTO);

    void setDailyGuess(UserGuessDTO userGuessDTO, DailyChallengeDTO dailyChallengeDTO);

    double getDailyGuessDistance(DailyChallengeDTO dailyChallengeDTO, UserGuessDTO userGuessDTO);
}
