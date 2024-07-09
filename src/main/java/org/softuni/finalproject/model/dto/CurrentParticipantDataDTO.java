package org.softuni.finalproject.model.dto;

public class CurrentParticipantDataDTO {

    private String username;
    private PictureLocationDTO picture;
    private UserGuessDTO userGuessDTO;
    private int score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PictureLocationDTO getPicture() {
        return picture;
    }

    public void setPicture(PictureLocationDTO picture) {
        this.picture = picture;
    }

    public UserGuessDTO getUserGuessDTO() {
        return userGuessDTO;
    }

    public void setUserGuessDTO(UserGuessDTO userGuessDTO) {
        this.userGuessDTO = userGuessDTO;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
