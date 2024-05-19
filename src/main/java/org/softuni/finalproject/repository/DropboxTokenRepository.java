package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.DropboxTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DropboxTokenRepository extends JpaRepository<DropboxTokenEntity, Long> {
}
