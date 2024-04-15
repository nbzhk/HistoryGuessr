package org.softuni.finalproject.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "scores")
public class ScoreEntity extends BaseEntity {

    @Column(nullable = false)
    private int score;
    @OneToOne
    private UserEntity player;
    @OneToOne
    private GameSessionEntity gameSession;
    @Column
    private LocalDateTime timestamp;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UserEntity getPlayer() {
        return player;
    }

    public void setPlayer(UserEntity player) {
        this.player = player;
    }

    public GameSessionEntity getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSessionEntity gameSession) {
        this.gameSession = gameSession;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
