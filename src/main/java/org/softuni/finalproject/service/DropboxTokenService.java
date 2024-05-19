package org.softuni.finalproject.service;

public interface DropboxTokenService {

    void setAccessTokenToAdmin(String token, Long expiresAt);
}
