package org.softuni.finalproject.model.dto;

import org.softuni.finalproject.model.UserGuess;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Arrays;

public class GameSessionDTO implements Serializable {
    private static final int ROUNDS_PER_GAME = 5;

    private User user;
    private final UserGuess[] userGuesses;
    private PictureLocationDTO[] pictureLocationDTOS;
    private final int[] roundScores;
    private final int[] yearDifferences;
    private final double[] distanceDifferences;
    private int round;

    public GameSessionDTO(User user, PictureLocationDTO[] pictureLocationDTOS) {
        this.user = user;
        this.userGuesses = new UserGuess[ROUNDS_PER_GAME];
        this.pictureLocationDTOS = pictureLocationDTOS;
        this.roundScores = new int[ROUNDS_PER_GAME];
        this.yearDifferences = new int[ROUNDS_PER_GAME];
        this.distanceDifferences = new double[ROUNDS_PER_GAME];
        this.round = 1;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserGuess[] getUserGuesses() {
        return this.userGuesses;
    }

    public void setUserGuess(UserGuess userGuess) {
        this.userGuesses[round - 1] = userGuess;
    }

    public PictureLocationDTO[] getPictureLocations() {
        return this.pictureLocationDTOS;
    }

    public void setPictureLocations(PictureLocationDTO[] pictureLocationDTOS) {
        this.pictureLocationDTOS = pictureLocationDTOS;
    }


    public int[] getScores() {
        return roundScores;
    }

    public int getRoundScore(){
        return this.roundScores[round - 1];
    }

    public void setRoundScore(int roundScore) {
        this.roundScores[round - 1] = roundScore;
    }

    public int getRound() {
        return round;
    }

    public void nextRound() {
        if (round <= ROUNDS_PER_GAME) {
            round++;
        } else {
            round = ROUNDS_PER_GAME;
        }
    }

    public int getTotalScore(){
      return Arrays.stream(this.roundScores).sum();
    }

    public void setRoundYearDifference(int roundYearDifference){
        this.yearDifferences[round - 1] = roundYearDifference;
    }

    public void setRoundDistanceDifference(double distance){
        this.distanceDifferences[round - 1] = distance;
    }

    public int getRoundYearDifference() {
        return yearDifferences[round - 1];
    }

    public double getRoundDistanceDifference() {
        return distanceDifferences[round - 1];
    }

    public int[] getYearDifferences() {
        return yearDifferences;
    }

    public double[] getDistanceDifferences() {
        return distanceDifferences;
    }

    public boolean lastRound() {
        return round > ROUNDS_PER_GAME;
    }
}
