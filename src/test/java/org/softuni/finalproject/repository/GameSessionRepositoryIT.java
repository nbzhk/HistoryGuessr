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
        firstGameSession.setRoundScores(List.of(10, 10, 10, 10, 10));

        gameSessionRepository.save(firstGameSession);

        GameSessionEntity secondGameSession = new GameSessionEntity();
        secondGameSession.setPlayer(user);
        secondGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        secondGameSession.setRoundScores(List.of(20, 20, 20, 20, 20));

        gameSessionRepository.save(secondGameSession);

        GameSessionEntity thirdGameSession = new GameSessionEntity();
        thirdGameSession.setPlayer(user);
        thirdGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        thirdGameSession.setRoundScores(List.of(30, 30, 30, 30, 30));

        gameSessionRepository.save(thirdGameSession);

        GameSessionEntity fourthGameSession = new GameSessionEntity();
        fourthGameSession.setPlayer(user);
        fourthGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        fourthGameSession.setRoundScores(List.of(40, 40, 40, 40, 40));


        gameSessionRepository.save(fourthGameSession);

        GameSessionEntity fifthGameSession = new GameSessionEntity();
        fifthGameSession.setPlayer(user);
        fifthGameSession.setTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0));
        fifthGameSession.setRoundScores(List.of(50, 50, 50, 50, 50));

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

        List<Integer> gameScores = new ArrayList<>();
        for (Object[] object : result) {
            gameScores.add((Integer) object[1]);
        }

        for (int i = 0; i < gameScores.size() - 1; i++) {
            assertThat(gameScores.get(i)).isGreaterThanOrEqualTo(gameScores.get(i + 1));
        }
    }
}
