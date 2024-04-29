package org.softuni.finalproject.service.impl;

import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.CurrentGameDTO;
import org.softuni.finalproject.service.GameSession;
import org.springframework.stereotype.Service;

@Service
public class GameSessionImpl implements GameSession {

    private static final double EARTH_RADIUS = 6371;
    private static final int MAX_YEAR_DIFFERENCE = 124;
    private static final double MAX_DISTANCE_KM = 20037.5;
    private final CurrentGameDTO currentGameDTO;

    public GameSessionImpl(CurrentGameDTO currentGameDTO) {
        this.currentGameDTO = currentGameDTO;
    }

    @Override
    public void setUserGuess(UserGuess userGuess) {
        this.currentGameDTO.setUserGuess(userGuess);

    }

    @Override
    public UserGuess getUserGuess() {
        return this.currentGameDTO.getUserGuesses()[this.currentGameDTO.getRound() - 1];
    }

    @Override
    public CurrentGameDTO getGameSession() {
        return this.currentGameDTO;
    }

    @Override
    public PictureLocation getCurrentLocation() {
        return this.currentGameDTO.getPictureLocations()[this.currentGameDTO.getRound()];
    }

    @Override
    public void calculateResult() {
        int roundScore = 0;
        int yearDiff = calculateYearDifference(this.currentGameDTO.getRound());
        Double distanceInKm = calculateDistanceInKm(this.currentGameDTO.getRound());

        double yearRatio = (double) yearDiff / MAX_YEAR_DIFFERENCE;
        roundScore += (int) (2500 * (1- yearRatio));

        if (distanceInKm != null) {
            double distanceRatio =  distanceInKm / MAX_DISTANCE_KM;
            roundScore += (int) (2500 * (1 - distanceRatio));
        }

        this.currentGameDTO.addRoundScore(roundScore);
        System.out.println("ROUND SCORE: " + roundScore);
        this.currentGameDTO.nextRound();
    }


    private int calculateYearDifference(int round) {

        int guessYear = this.currentGameDTO.getUserGuesses()[round].getGuessYear();
        int actualYear = this.currentGameDTO.getPictureLocations()[round].getYear();

        return Math.abs(guessYear - actualYear);
    }

    private Double calculateDistanceInKm(int round) {
        double actualLatitude = this.currentGameDTO.getPictureLocations()[round].getLatitude();
        double actualLongitude = this.currentGameDTO.getPictureLocations()[round].getLongitude();
        Double guessLat = this.currentGameDTO.getUserGuesses()[round].getGuessLat();
        Double guessLng = this.currentGameDTO.getUserGuesses()[round].getGuessLng();

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
