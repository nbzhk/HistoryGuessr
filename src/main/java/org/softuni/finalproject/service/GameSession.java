package org.softuni.finalproject.service;

import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.CurrentGame;

public interface GameSession {

    void setUserGuess(UserGuess userGuess);
    UserGuess getUserGuess();

    CurrentGame getGameSession();

    void calculateResult();
}
