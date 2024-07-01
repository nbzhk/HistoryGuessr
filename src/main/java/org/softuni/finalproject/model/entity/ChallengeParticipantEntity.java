package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "challenge_participants")
public class ChallengeParticipantEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "daily_challenge_id")
    private DailyChallengeEntity dailyChallenge;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column
    private int score;

    public DailyChallengeEntity getDailyChallenge() {
        return dailyChallenge;
    }

    public void setDailyChallenge(DailyChallengeEntity dailyChallenge) {
        this.dailyChallenge = dailyChallenge;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
