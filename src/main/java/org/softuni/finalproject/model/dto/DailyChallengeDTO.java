package org.softuni.finalproject.model.dto;

import java.util.List;

public class DailyChallengeDTO {
    private Long id;
    private PictureLocationDTO picture;
    private List<ChallengeParticipantDTO> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PictureLocationDTO getPicture() {
        return picture;
    }

    public void setPicture(PictureLocationDTO picture) {
        this.picture = picture;
    }

    public List<ChallengeParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ChallengeParticipantDTO> participants) {
        this.participants = participants;
    }
}

