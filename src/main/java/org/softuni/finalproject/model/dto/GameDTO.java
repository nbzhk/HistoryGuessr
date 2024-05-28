package org.softuni.finalproject.model.dto;

import org.softuni.finalproject.model.UserGuess;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Arrays;

public class GameDTO implements Serializable {
    private User user;
    private final UserGuess[] userGuesses;
    private PictureLocationDTO[] pictureLocationDTOS;
    private final int[] score;
    private int round;

    public GameDTO(User user, PictureLocationDTO[] pictureLocationDTOS) {
        this.user = user;
        this.userGuesses = new UserGuess[5];
        this.pictureLocationDTOS = pictureLocationDTOS;
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

    public PictureLocationDTO[] getPictureLocations() {
        return this.pictureLocationDTOS;
    }

    public void setPictureLocations(PictureLocationDTO[] pictureLocationDTOS) {
        this.pictureLocationDTOS = pictureLocationDTOS;
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

    public int getTotalScore(){
      return Arrays.stream(this.score).sum();
    }
}
