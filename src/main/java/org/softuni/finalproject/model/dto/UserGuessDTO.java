package org.softuni.finalproject.model.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserGuessDTO {
    private Double guessLat;
    private Double guessLng;
    private int guessYear;

    public Double getGuessLat() {
        return guessLat;
    }

    public void setGuessLat(double guessLat) {
        this.guessLat = guessLat;
    }

    public Double getGuessLng() {
        return guessLng;
    }

    public void setGuessLng(double guessLng) {
        this.guessLng = guessLng;
    }

    public int getGuessYear() {
        return guessYear;
    }

    public void setGuessYear(int guessYear) {
        this.guessYear = guessYear;
    }
}
