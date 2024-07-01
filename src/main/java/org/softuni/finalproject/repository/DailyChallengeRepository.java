package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.DailyChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyChallengeRepository extends JpaRepository<DailyChallengeEntity, Long> {

    boolean existsByDate(LocalDate now);

    DailyChallengeEntity findByDate(LocalDate now);
}
