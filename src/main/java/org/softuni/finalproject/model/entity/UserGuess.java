package org.softuni.finalproject.model.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserGuess {

    private Double guessLat;
    private Double guessLng;
    private int guessYear;

    public Double getGuessLat() {
        return guessLat;
    }

    public void setGuessLat(Double guessLat) {
        this.guessLat = guessLat;
    }

    public Double getGuessLng() {
        return guessLng;
    }

    public void setGuessLng(Double guessLng) {
        this.guessLng = guessLng;
    }

    public int getGuessYear() {
        return guessYear;
    }

    public void setGuessYear(int guessYear) {
        this.guessYear = guessYear;
    }
}
