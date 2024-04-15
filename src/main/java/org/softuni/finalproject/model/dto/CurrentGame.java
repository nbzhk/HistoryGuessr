package org.softuni.finalproject.model.dto;

import org.softuni.finalproject.model.CurrentUser;
import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;

public class CurrentGame {
    private CurrentUser user;
    private UserGuess userGuess;
    private PictureLocation pictureLocation;
    private int score = 0;

    public CurrentUser getUser() {
        return user;
    }

    public void setUser(CurrentUser user) {
        this.user = user;
    }

    public UserGuess getUserGuess() {
        return userGuess;
    }

    public void setUserGuess(UserGuess userGuess) {
        this.userGuess = userGuess;
    }

    public PictureLocation getPictureLocation() {
        return pictureLocation;
    }

    public void setPictureLocation(PictureLocation pictureLocation) {
        this.pictureLocation = pictureLocation;
    }

    public int getScore() {
        return score;
    }

    public void addToScore(int roundScore) {
        this.score += roundScore;
    }
}
