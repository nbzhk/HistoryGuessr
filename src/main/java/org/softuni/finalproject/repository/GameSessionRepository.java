package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSessionEntity, Long> {
}
