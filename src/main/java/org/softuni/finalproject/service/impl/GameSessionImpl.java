package org.softuni.finalproject.service.impl;

import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.service.GameSession;
import org.springframework.stereotype.Service;

@Service
public class GameSessionImpl implements GameSession {

    private static final double EARTH_RADIUS = 6371;
    private static final int MAX_YEAR_DIFFERENCE = 124;
    private static final double MAX_DISTANCE_KM = 20037.5;
    private final GameDTO gameDTO;

    public GameSessionImpl(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }

    @Override
    public void setUserGuess(UserGuess userGuess) {
        this.gameDTO.setUserGuess(userGuess);

    }

    @Override
    public UserGuess getUserGuess() {
        return this.gameDTO.getUserGuesses()[this.gameDTO.getRound() - 1];
    }

    @Override
    public GameDTO getGameSession() {
        return this.gameDTO;
    }

    @Override
    public PictureLocation getCurrentLocation() {
        return this.gameDTO.getPictureLocations()[this.gameDTO.getRound()];
    }

    @Override
    public void calculateResult() {
        int roundScore = 0;
        int yearDiff = calculateYearDifference(this.gameDTO.getRound());
        Double distanceInKm = calculateDistanceInKm(this.gameDTO.getRound());

        double yearRatio = (double) yearDiff / MAX_YEAR_DIFFERENCE;
        roundScore += (int) (2500 * (1- yearRatio));

        if (distanceInKm != null) {
            double distanceRatio =  distanceInKm / MAX_DISTANCE_KM;
            roundScore += (int) (2500 * (1 - distanceRatio));
        }

        this.gameDTO.addRoundScore(roundScore);
        System.out.println("ROUND SCORE: " + roundScore);
        this.gameDTO.nextRound();
    }


    private int calculateYearDifference(int round) {

        int guessYear = this.gameDTO.getUserGuesses()[round].getGuessYear();
        int actualYear = this.gameDTO.getPictureLocations()[round].getYear();

        return Math.abs(guessYear - actualYear);
    }

    private Double calculateDistanceInKm(int round) {
        double actualLatitude = this.gameDTO.getPictureLocations()[round].getLatitude();
        double actualLongitude = this.gameDTO.getPictureLocations()[round].getLongitude();
        Double guessLat = this.gameDTO.getUserGuesses()[round].getGuessLat();
        Double guessLng = this.gameDTO.getUserGuesses()[round].getGuessLng();

        if (guessLat != null && guessLng != null) {
            double actualLatRad = Math.toRadians(actualLatitude);
            double actualLngRad = Math.toRadians(actualLongitude);
            double guessLatRad = Math.toRadians(guessLat);
            double guessLngRad = Math.toRadians(guessLng);

            double diffLat = actualLatRad - guessLatRad;
            double diffLng = actualLngRad - guessLngRad;

            // Haversine formula
            double a = Math.pow(Math.sin(diffLat / 2), 2) +
                    Math.cos(actualLatRad) * Math.cos(guessLatRad) *
                            Math.pow(Math.sin(diffLng / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return EARTH_RADIUS * c;
        }

        return null;
    }


}
