package org.softuni.finalproject.service.impl;

import org.softuni.finalproject.model.entity.DropboxTokenEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.DropboxTokenRepository;
import org.softuni.finalproject.service.DropboxTokenService;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DropboxTokenServiceImpl implements DropboxTokenService {

    private final DropboxTokenRepository dropboxTokenRepository;
    private final UserAuthService userAuthService;
    private final UserService userService;


    public DropboxTokenServiceImpl(DropboxTokenRepository dropboxTokenRepository, UserAuthService userAuthService, UserService userService) {
        this.dropboxTokenRepository = dropboxTokenRepository;
        this.userAuthService = userAuthService;
        this.userService = userService;
    }

    @Override
    public void setAccessTokenToAdmin(String accessToken, Long expiresIn) {
        String currentUsername = this.userAuthService.getCurrentUsername();

        if (currentUsername == null) {
            return;
        }

        Optional<UserEntity> user = this.userService.findByUsername(currentUsername);

        if (user.isEmpty()) {
            return;
        }

        UserEntity userEntity = user.get();
        DropboxTokenEntity newToken = this.createToken(accessToken, expiresIn);

        if (shouldUpdateToken(userEntity.getDropboxToken(), newToken)) {
            removeOldToken(userEntity);
            userEntity.setDropboxToken(newToken);
            this.userService.saveUser(userEntity);
        }

    }

    private void removeOldToken(UserEntity userEntity) {
        DropboxTokenEntity dropboxToken = userEntity.getDropboxToken();
        if (dropboxToken != null) {
            userEntity.setDropboxToken(null);
            this.dropboxTokenRepository.delete(dropboxToken);
        }
    }

    private DropboxTokenEntity createToken(String token, Long expiresIn) {
        DropboxTokenEntity tokenEntity = new DropboxTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setExpiresIn(expiresIn);
        tokenEntity.setCreationTime(LocalDateTime.now());

        this.dropboxTokenRepository.save(tokenEntity);
        return tokenEntity;
    }

    private boolean shouldUpdateToken(DropboxTokenEntity currentToken, DropboxTokenEntity newToken) {
        return currentToken == null ||
                currentToken.getToken() == null ||
                currentToken.getExpiresIn() == null ||
                currentToken.getCreationTime() == null ||
                !currentToken.getToken().equals(newToken.getToken());
    }
}
