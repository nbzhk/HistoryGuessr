package org.softuni.finalproject.service.impl;

import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.CurrentGame;
import org.softuni.finalproject.service.GameSession;
import org.springframework.stereotype.Service;

@Service
public class GameSessionImpl implements GameSession {

    private static final double EARTH_RADIUS = 6371;
    private static final int MAX_YEAR_DIFFERENCE = 124;
    private static final double MAX_DISTANCE_KM = 20037.5;
    private final CurrentGame currentGame;

    public GameSessionImpl(CurrentGame currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public void setUserGuess(UserGuess userGuess) {
        this.currentGame.setUserGuess(userGuess);

        System.out.println(userGuess);

        System.out.println();

    }

    @Override
    public UserGuess getUserGuess() {
        return this.currentGame.getUserGuess();
    }

    @Override
    public CurrentGame getGameSession() {
        return this.currentGame;
    }

    @Override
    public void calculateResult() {
        int roundScore = 0;
        int yearDiff = calculateYearDifference();
        Double distanceInKm = calculateDistanceInKm();

        double yearRatio = (double) yearDiff / MAX_YEAR_DIFFERENCE;
        roundScore += (int) (2500 * (1- yearRatio));

        if (distanceInKm != null) {
            double distanceRatio =  distanceInKm / MAX_DISTANCE_KM;
            roundScore += (int) (2500 * (1 - distanceRatio));
        }

        System.out.println("ROUND SCORE: " + roundScore);
    }

    private int calculateYearDifference() {
        int guessYear = this.currentGame.getUserGuess().getGuessYear();
        int actualYear = this.currentGame.getPictureLocation().getYear();

        return Math.abs(guessYear - actualYear);
    }

    private Double calculateDistanceInKm() {
        double actualLatitude = this.currentGame.getPictureLocation().getLatitude();
        double actualLongitude = this.currentGame.getPictureLocation().getLongitude();
        Double guessLat = this.currentGame.getUserGuess().getGuessLat();
        Double guessLng = this.currentGame.getUserGuess().getGuessLng();

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
