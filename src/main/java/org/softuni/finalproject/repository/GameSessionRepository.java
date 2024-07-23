package org.softuni.finalproject.repository;

import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameSessionRepository extends JpaRepository<GameSessionEntity, Long> {

    @Query(value = "SELECT timestamp , g.totalScore FROM GameSessionEntity g " +
            "WHERE g.player.id = :playerId " +
            "GROUP BY g.id " +
            "ORDER BY g.totalScore DESC " +
            "LIMIT 5")
    List<Object[]> findTopFiveGamesForPlayer(@Param("playerId") Long playerId);
}
