package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.dto.UserGuessDTO;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GameServiceIntegrationTest {

    private static final double EARTH_RADIUS = 6371;
    private static final int MAX_YEAR_DIFFERENCE = 124;
    private static final double MAX_DISTANCE_KM = 20037.5;

    @Autowired
    private GameServiceImpl gameService;

    @Mock
    private PictureService pictureService;

    @Mock
    private DailyChallengeAPIService dailyChallengeAPIService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testCalculateRoundScore() {
        // Arrange
        int round = 1;
        int guessYear = 2000;
        int actualYear = 1990;
        double guessLat = 42.6977;
        double guessLng = 23.3219;
        double actualLat = 42.6977;
        double actualLng = 23.3219;

        User mockUser = new User("testUser", "testPass", new ArrayList<>());

        GameSessionDTO gameSessionDTO = new GameSessionDTO(mockUser, new PictureLocationDTO[]{});
        PictureLocationDTO pictureLocationDTO = new PictureLocationDTO();
        UserGuessDTO userGuessDTO = new UserGuessDTO();

        gameSessionDTO.setRound(round);
        gameSessionDTO.setUserGuess(userGuessDTO);
        gameSessionDTO.setPictureLocations(new PictureLocationDTO[]{pictureLocationDTO});
        userGuessDTO.setGuessYear(guessYear);
        userGuessDTO.setGuessLat(guessLat);
        userGuessDTO.setGuessLng(guessLng);
        pictureLocationDTO.setYear(actualYear);
        pictureLocationDTO.setLatitude(actualLat);
        pictureLocationDTO.setLongitude(actualLng);

        // Act
        gameService.calculateRoundScore(gameSessionDTO);

        // Assert
        int expectedYearDifference = Math.abs(guessYear - actualYear);
        double expectedDistanceInKm = haversineFormula(actualLat, actualLng, guessLat, guessLng);

        int yearScore = 5000 * (1 - (expectedYearDifference / MAX_YEAR_DIFFERENCE));
        int distanceScore = (int) (5000 * (1 - (expectedDistanceInKm / MAX_DISTANCE_KM)));
        int expectedRoundScore = yearScore + distanceScore;

        assertEquals(expectedRoundScore, gameSessionDTO.getRoundScore());
    }

    private double haversineFormula(double actualLatitude,
                                    double actualLongitude,
                                    double guessLat,
                                    double guessLng) {
        double latDistance = Math.toRadians(guessLat - actualLatitude);
        double lngDistance = Math.toRadians(guessLng - actualLongitude);

        double a = Math.pow(Math.sin(latDistance / 2), 2) +
                Math.cos(Math.toRadians(actualLatitude))
                        * Math.cos(Math.toRadians(guessLat))
                        * Math.pow(Math.sin(lngDistance / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}