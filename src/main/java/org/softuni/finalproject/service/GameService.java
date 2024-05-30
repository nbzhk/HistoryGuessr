package org.softuni.finalproject.service;

import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameSessionDTO;

public interface GameService {

    GameSessionDTO startGame(HttpSession session);

    void setUserGuess(UserGuess userGuess, GameSessionDTO gameSessionDTO);

    void calculateRoundScore(GameSessionDTO gameSessionDTO);

    PictureLocationDTO getCurrentLocation(GameSessionDTO gameSessionDTO);

    GameSessionDTO getCurrentGame(HttpSession session);

}
