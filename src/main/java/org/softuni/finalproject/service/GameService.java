package org.softuni.finalproject.service;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.*;

public interface GameService {

    GameSessionDTO startGame(HttpSession session);

    void setUserGuess(UserGuessDTO userGuessDTO, GameSessionDTO gameSessionDTO);

    void calculateRoundScore(GameSessionDTO gameSessionDTO);

    PictureLocationDTO getCurrentLocation(GameSessionDTO gameSessionDTO, Integer round);

    GameSessionDTO getCurrentGame(HttpSession session);

    void calculateDailyScore(DailyChallengeDTO dailyChallengeDTO);

    void setDailyGuess(UserGuessDTO userGuessDTO, DailyChallengeDTO dailyChallengeDTO);

    double getDailyGuessDistance(DailyChallengeDTO dailyChallengeDTO, UserGuessDTO userGuessDTO);

}
