package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "daily_challenges")
public class DailyChallengeEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "picture_id")
    private PictureEntity picture;
    @OneToMany(mappedBy = "dailyChallenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeParticipantEntity> participants;
    @Column(nullable = false, unique = true)
    private LocalDate date;

    public DailyChallengeEntity() {
        this.participants = new ArrayList<>();
    }

    public DailyChallengeEntity(PictureEntity picture, LocalDate date) {
        this();
        this.picture = picture;
        this.date = date;
    }

    public PictureEntity getPicture() {
        return picture;
    }

    public void setPicture(PictureEntity picture) {
        this.picture = picture;
    }

    public List<ChallengeParticipantEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ChallengeParticipantEntity> participants) {
        this.participants = participants;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
