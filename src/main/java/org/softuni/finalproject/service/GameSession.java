package org.softuni.finalproject.service;

import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.CurrentGameDTO;

public interface GameSession {

    void setUserGuess(UserGuess userGuess);
    UserGuess getUserGuess();

    CurrentGameDTO getGameSession();

    void calculateResult();

    PictureLocation getCurrentLocation();
}
