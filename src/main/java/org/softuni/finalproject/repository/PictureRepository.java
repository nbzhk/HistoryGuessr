package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    PictureEntity findByUrl(String url);
}
