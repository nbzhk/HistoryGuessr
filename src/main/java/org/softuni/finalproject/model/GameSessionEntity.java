package org.softuni.finalproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class GameSessionEntity extends BaseEntity {
    @OneToOne(targetEntity = UserEntity.class)
    private UserEntity player;
    @OneToOne(targetEntity = LocationEntity.class)
    private LocationEntity location;
    @OneToOne(targetEntity = PictureEntity.class)
    private PictureEntity picture;
    @Column(name = "player_guess_latitude")
    private double playerGuessLatitude;
    @Column(name = "player_guess_longitude")
    private double playerGuessLongitude;
    @Column(name = "player_guess_year")
    private int playerGuessYear;
    @Column
    private int score;
    @Column
    private LocalDateTime timestamp;

    public UserEntity getPlayer() {
        return player;
    }

    public void setPlayer(UserEntity player) {
        this.player = player;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public PictureEntity getPicture() {
        return picture;
    }

    public void setPicture(PictureEntity picture) {
        this.picture = picture;
    }

    public double getPlayerGuessLatitude() {
        return playerGuessLatitude;
    }

    public void setPlayerGuessLatitude(double playerGuessLatitude) {
        this.playerGuessLatitude = playerGuessLatitude;
    }

    public double getPlayerGuessLongitude() {
        return playerGuessLongitude;
    }

    public void setPlayerGuessLongitude(double playerGuessLongitude) {
        this.playerGuessLongitude = playerGuessLongitude;
    }

    public int getPlayerGuessYear() {
        return playerGuessYear;
    }

    public void setPlayerGuessYear(int playerGuessYear) {
        this.playerGuessYear = playerGuessYear;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
