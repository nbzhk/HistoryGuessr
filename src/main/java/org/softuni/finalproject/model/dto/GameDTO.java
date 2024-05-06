package org.softuni.finalproject.model.dto;

import org.softuni.finalproject.model.CurrentUser;
import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;
import org.springframework.security.core.userdetails.User;

public class GameDTO {
    private User user;
    private UserGuess[] userGuesses = new UserGuess[5];
    private PictureLocation[] pictureLocations;
    private final int[] score;
    private int round;

    public GameDTO(User user, PictureLocation[] pictureLocations) {
        this.user = user;
        this.userGuesses = new UserGuess[5];
        this.pictureLocations = pictureLocations;
        this.score = new int[5];
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
        this.score[round - 1] = roundScore;
    }

    public int getRound() {
        return round;
    }

    public void nextRound() {
        round++;
    }
}
