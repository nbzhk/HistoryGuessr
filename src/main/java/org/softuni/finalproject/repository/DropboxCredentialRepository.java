package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.DropboxCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DropboxCredentialRepository extends JpaRepository<DropboxCredentialEntity, Long> {

    DropboxCredentialEntity getDropboxCredentialByRefreshToken(String refreshToken);
}
