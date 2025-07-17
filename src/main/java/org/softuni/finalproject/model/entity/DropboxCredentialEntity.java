package org.softuni.finalproject.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "dropbox_credentials")
public class DropboxCredentialEntity extends BaseEntity {


    @Column(name = "encoded_access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;
    @Column(name = "encoded_refresh_token", nullable = false)
    private String refreshToken;
    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;



    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}

