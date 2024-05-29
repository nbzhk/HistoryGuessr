package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    PictureEntity findByUrl(String url);

    @Query(value = "SELECT * FROM pictures ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<PictureEntity> findRandomPictures(@Param("limit") int limit);
}
