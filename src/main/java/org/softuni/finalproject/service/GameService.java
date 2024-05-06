package org.softuni.finalproject.service;

import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameDTO;

public interface GameService {

    void setUserGuess(UserGuess userGuess);
    UserGuess getUserGuess();

    GameDTO getGameSession();

    void calculateResult();

    PictureLocation getCurrentLocation();

    void startGame();

    void nextRound();
}
