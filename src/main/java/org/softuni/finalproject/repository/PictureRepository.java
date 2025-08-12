package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    PictureEntity findByUrl(String url);

    @Query(value = "SELECT * FROM pictures ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<PictureEntity> findRandomPictures(@Param("limit") int limit);

    @Query(value = "select * from pictures ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    Optional<PictureEntity> findRandomDailyPicture(@Param("limit") int limit);

    @Query(value = "select id from pictures", nativeQuery = true)
    List<Long> getAllIds();
}
