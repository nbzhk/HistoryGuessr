package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class GameSessionEntity extends BaseEntity {
    @ManyToOne
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


}
