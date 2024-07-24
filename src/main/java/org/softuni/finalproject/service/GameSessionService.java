package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.RoundInfoDTO;

public interface GameSessionService {

    void saveGameSession(GameSessionDTO gameSessionDTO);

    RoundInfoDTO getRoundInfo(int round);
}
