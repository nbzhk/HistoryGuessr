package org.softuni.finalproject.model.dto;

public class RoundInfoDTO {
    private int round;
    private UserGuessDTO userGuessDTO;
    private PictureLocationDTO pictureLocationDTO;
    private int yearDiff;
    private int score;
    private double distanceDiff;

    public void setYearDiff(int yearDiff) {
        this.yearDiff = yearDiff;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getDistanceDiff() {
        return distanceDiff;
    }

    public void setDistanceDiff(double distanceDiff) {
        this.distanceDiff = distanceDiff;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setUserGuessDTO(UserGuessDTO userGuessDTO) {
        this.userGuessDTO = userGuessDTO;
    }

    public UserGuessDTO getUserGuessDTO() {
        return userGuessDTO;
    }

    public PictureLocationDTO getPictureLocationDTO() {
        return pictureLocationDTO;
    }

    public void setPictureLocationDTO(PictureLocationDTO pictureLocationDTO) {
        this.pictureLocationDTO = pictureLocationDTO;
    }


    public int getYearDiff() {
        return this.yearDiff;
    }
}
