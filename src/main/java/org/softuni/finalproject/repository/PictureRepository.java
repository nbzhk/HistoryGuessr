package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    PictureEntity findByUrl(String url);

    @Query(value = "select id from pictures", nativeQuery = true)
    List<Long> getAllIds();
}
