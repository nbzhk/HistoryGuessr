package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameDTO;

public interface GameService {

    void setUserGuess(UserGuess userGuess);
    UserGuess getUserGuess();

    GameDTO getGameSession();

    void calculateResult();

    PictureLocationDTO getCurrentLocation();

    GameDTO startGame();

    boolean lastRound();

    int getRoundYearDifference();

    double getRoundDistance();

    void saveSession(GameDTO gameDTO);
}
