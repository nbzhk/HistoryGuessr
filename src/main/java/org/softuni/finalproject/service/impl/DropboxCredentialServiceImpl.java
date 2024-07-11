package org.softuni.finalproject.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import org.softuni.finalproject.model.entity.DropboxCredentialEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.DropboxCredentialRepository;
import org.softuni.finalproject.service.DropboxCredentialService;
import org.softuni.finalproject.service.UserAuthService;
import org.softuni.finalproject.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DropboxCredentialServiceImpl implements DropboxCredentialService {

    private final DropboxCredentialRepository dropboxCredentialRepository;
    private final UserAuthService userAuthService;
    private final UserService userService;
    private final DbxRequestConfig config;


    public DropboxCredentialServiceImpl(DropboxCredentialRepository dropboxCredentialRepository,
                                        UserAuthService userAuthService,
                                        UserService userService,
                                        DbxRequestConfig config) {
        this.dropboxCredentialRepository = dropboxCredentialRepository;
        this.userAuthService = userAuthService;
        this.userService = userService;
        this.config = config;
    }

    @Override
    public boolean checkCredentialValidation(DbxCredential credential) throws DbxException {
        if (credential.aboutToExpire()) {
            credential.refresh(config);
            updateDropboxCredential(credential);
            return false;
        }

        return true;
    }

    private void updateDropboxCredential(DbxCredential credential) {

        DropboxCredentialEntity currentDropboxCredentialEntity = getCurrentDropboxCredential(credential);

        if (currentDropboxCredentialEntity != null) {

            currentDropboxCredentialEntity.setAccessToken(credential.getAccessToken());
            currentDropboxCredentialEntity.setExpiresAt(credential.getExpiresAt());

            this.dropboxCredentialRepository.save(currentDropboxCredentialEntity);
        }
    }

    @Override
    public void setCredentialToAdmin(DbxCredential credential) {
        Optional<UserEntity> userEntity = getCurrentUser();
        if (userEntity.isEmpty()) return;

        DropboxCredentialEntity newCredential = this.createCredential(credential);

        if (shouldUpdateCredential(userEntity.get().getDropboxCredential(), newCredential)) {
            removeOldCredential(userEntity.get());
            userEntity.get().setDropboxCredential(newCredential);
            this.userService.saveUser(userEntity.get());
        }
    }

    private boolean shouldUpdateCredential(DropboxCredentialEntity oldCredential, DropboxCredentialEntity newCredential) {
        return oldCredential == null ||
                !oldCredential.getAccessToken().equals(newCredential.getAccessToken()) ||
                !oldCredential.getRefreshToken().equals(newCredential.getRefreshToken());
    }

    private DropboxCredentialEntity createCredential(DbxCredential credential) {
        DropboxCredentialEntity credentialEntity = new DropboxCredentialEntity();
        credentialEntity.setAccessToken(credential.getAccessToken());
        credentialEntity.setRefreshToken(credential.getRefreshToken());
        credentialEntity.setExpiresAt(credential.getExpiresAt());

        this.dropboxCredentialRepository.save(credentialEntity);
        return credentialEntity;
    }

    private void removeOldCredential(UserEntity userEntity) {
        DropboxCredentialEntity credential = userEntity.getDropboxCredential();
        if (credential != null) {
            userEntity.setDropboxCredential(null);
            this.dropboxCredentialRepository.delete(credential);
        }
    }

    private DropboxCredentialEntity getCurrentDropboxCredential(DbxCredential credential) {
       return this.dropboxCredentialRepository.getDropboxCredentialByRefreshToken(credential.getRefreshToken());
    }

    private Optional<UserEntity> getCurrentUser() {
        String currentUsername = this.userAuthService.getCurrentUsername();

        if (currentUsername == null) {
            return Optional.empty();
        }

        return this.userService.findByUsername(currentUsername);
    }
}
