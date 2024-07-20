package org.softuni.finalproject.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GameSessionRepositoryIT {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@test.com");
        user.setRegistrationDate(LocalDate.of(2024, 1, 1));
        this.userRepository.save(user);

        GameSessionEntity firstGameSession = new GameSessionEntity();
        firstGameSession.setPlayer(user);
        firstGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        firstGameSession.setPictures(new ArrayList<>());
        firstGameSession.setGuesses(new ArrayList<>());
        firstGameSession.setYearDifferences(new ArrayList<>());
        firstGameSession.setRoundScores(List.of(10,10,10,10,10));
        firstGameSession.setDistanceDifferences(new ArrayList<>());
        gameSessionRepository.save(firstGameSession);

        GameSessionEntity secondGameSession = new GameSessionEntity();
        secondGameSession.setPlayer(user);
        secondGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        secondGameSession.setPictures(new ArrayList<>());
        secondGameSession.setGuesses(new ArrayList<>());
        secondGameSession.setYearDifferences(new ArrayList<>());
        secondGameSession.setRoundScores(List.of(20,20,20,20,20));
        secondGameSession.setDistanceDifferences(new ArrayList<>());
        gameSessionRepository.save(secondGameSession);

        GameSessionEntity thirdGameSession = new GameSessionEntity();
        thirdGameSession.setPlayer(user);
        thirdGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        thirdGameSession.setPictures(new ArrayList<>());
        thirdGameSession.setGuesses(new ArrayList<>());
        thirdGameSession.setYearDifferences(new ArrayList<>());
        thirdGameSession.setRoundScores(List.of(30,30,30,30,30));
        thirdGameSession.setDistanceDifferences(new ArrayList<>());
        gameSessionRepository.save(thirdGameSession);

        GameSessionEntity fourthGameSession = new GameSessionEntity();
        fourthGameSession.setPlayer(user);
        fourthGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        fourthGameSession.setPictures(new ArrayList<>());
        fourthGameSession.setGuesses(new ArrayList<>());
        fourthGameSession.setYearDifferences(new ArrayList<>());
        fourthGameSession.setRoundScores(List.of(40,40,40,40,40));
        fourthGameSession.setDistanceDifferences(new ArrayList<>());
        gameSessionRepository.save(fourthGameSession);

        GameSessionEntity fifthGameSession = new GameSessionEntity();
        fifthGameSession.setPlayer(user);
        fifthGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        fifthGameSession.setPictures(new ArrayList<>());
        fifthGameSession.setGuesses(new ArrayList<>());
        fifthGameSession.setYearDifferences(new ArrayList<>());
        fifthGameSession.setRoundScores(List.of(50,50,50,50,50));
        fifthGameSession.setDistanceDifferences(new ArrayList<>());
        gameSessionRepository.save(fifthGameSession);

    }

    @Test
    @Transactional
    public void testFindTopFiveGamesForPlayer() {
        UserEntity user = this.userRepository.findAll().stream()
                .findFirst().orElse(null);

        assertThat(user).isNotNull();

        Long userId = user.getId();

        List<Object[]> result = this.gameSessionRepository.findTopFiveGamesForPlayer(userId);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(5);

        List<Long> totalScores = new ArrayList<>();
        for (Object[] object : result) {
            totalScores.add((Long) object[1]);
        }

        for (int i = 0; i < totalScores.size() - 1; i++) {
            assertThat(totalScores.get(i)).isGreaterThanOrEqualTo(totalScores.get(i + 1));
        }
    }
}
