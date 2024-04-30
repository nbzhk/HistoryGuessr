package org.softuni.finalproject.model.dto;

import org.softuni.finalproject.model.CurrentUser;
import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;

public class GameDTO {
    private CurrentUser user;
    private UserGuess[] userGuesses = new UserGuess[5];
    private PictureLocation[] pictureLocations;
    private int[] score = new int[5];
    private int round = 0;

    public CurrentUser getUser() {
        return user;
    }

    public void setUser(CurrentUser user) {
        this.user = user;
    }

    public UserGuess[] getUserGuesses() {
        return this.userGuesses;
    }

    public void setUserGuess(UserGuess userGuess) {
        this.userGuesses[round] = userGuess;
    }

    public PictureLocation[] getPictureLocations() {
        return this.pictureLocations;
    }

    public void setPictureLocations(PictureLocation[] pictureLocations) {
        this.pictureLocations = pictureLocations;
    }


    public int[] getScore() {
        return score;
    }

    public void addRoundScore(int roundScore) {
        this.score[round] = roundScore;
    }

    public int getRound() {
        return round;
    }

    public void nextRound() {
        if (round != 5) {
            round++;
        } else {
            round = 0;
        }

    }

}
