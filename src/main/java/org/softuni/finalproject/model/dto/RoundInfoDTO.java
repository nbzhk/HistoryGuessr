package org.softuni.finalproject.model.dto;

public class RoundInfoDTO {
    private int round;
    private UserGuessDTO userGuessDTO;
    private PictureLocationDTO pictureLocationDTO;

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

    //TODO fix this
    public double getDistance() {
        return Math.abs(2 -2 );
    }

    public int getYearDiff() {
        return Math.abs(this.userGuessDTO.getGuessYear() - pictureLocationDTO.getYear());
    }
}
