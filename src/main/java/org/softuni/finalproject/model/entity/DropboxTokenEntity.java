package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dropbox_tokens")
public class DropboxTokenEntity extends BaseEntity {


    @Column(name = "encoded_access_token")
    private String token;
    @Column(name = "expires_in")
    private Long expiresIn;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresAt) {
        this.expiresIn = expiresAt;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}

