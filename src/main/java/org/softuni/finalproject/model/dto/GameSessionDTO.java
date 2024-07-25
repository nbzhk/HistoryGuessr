package org.softuni.finalproject.model.dto;

import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;

public class GameSessionDTO implements Serializable {
    private static final int ROUNDS_PER_GAME = 5;

    private User user;
    private UserGuessDTO[] userGuessDTOS;
    private PictureLocationDTO[] pictureLocationDTOS;
    private int[] roundScores;
    private int[] yearDifferences;
    private double[] distanceDifferences;
    private int round;
    private LocalDate timestamp;


    public GameSessionDTO(User user, PictureLocationDTO[] pictureLocationDTOS) {
        this.user = user;
        this.userGuessDTOS = new UserGuessDTO[ROUNDS_PER_GAME];
        this.pictureLocationDTOS = pictureLocationDTOS;
        this.roundScores = new int[ROUNDS_PER_GAME];
        this.yearDifferences = new int[ROUNDS_PER_GAME];
        this.distanceDifferences = new double[ROUNDS_PER_GAME];
        this.round = 1;
    }

    public void setRoundScores(int[] roundScores) {
        this.roundScores = roundScores;
    }

    public UserGuessDTO[] getUserGuessDTOS() {
        return userGuessDTOS;
    }

    public void setUserGuesses(UserGuessDTO[] userGuessDTOS) {
        this.userGuessDTOS = userGuessDTOS;
    }

    public PictureLocationDTO[] getPictureLocationDTOS() {
        return pictureLocationDTOS;
    }

    public int[] getRoundScores() {
        return roundScores;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserGuessDTO[] getUserGuesses() {
        return this.userGuessDTOS;
    }

    public void setUserGuess(UserGuessDTO userGuessDTO) {
        this.userGuessDTOS[round - 1] = userGuessDTO;
    }

    public PictureLocationDTO[] getPictureLocations() {
        return this.pictureLocationDTOS;
    }

    public void setPictureLocations(PictureLocationDTO[] pictureLocationDTOS) {
        this.pictureLocationDTOS = pictureLocationDTOS;
    }

    public void setPictureLocationDTOS(PictureLocationDTO[] pictureLocationDTOS) {
        this.pictureLocationDTOS = pictureLocationDTOS;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int[] getScores() {
        return roundScores;
    }

    public int getRoundScore(int round){
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

    public int getRoundYearDifference(int round) {
        return yearDifferences[round - 1];
    }

    public double getRoundDistanceDifference(int round) {
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

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public void setDistanceDifferences(double[] distanceDifferences) {
        this.distanceDifferences = distanceDifferences;
    }

    public void setYearDifferences(int[] yearDifferences) {
        this.yearDifferences = yearDifferences;
    }

    public void setUserGuessDTOS(UserGuessDTO[] userGuessDTOS) {
        this.userGuessDTOS = userGuessDTOS;
    }
}
