package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.*;
import org.softuni.finalproject.model.entity.ChallengeParticipantEntity;
import org.softuni.finalproject.model.entity.DailyChallengeEntity;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.DailyChallengeRepository;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.GameService;
import org.softuni.finalproject.service.exception.DailyChallengeNotFound;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class DailyChallengeAPIServiceImplTest {

    @Mock
    private DailyChallengeRepository dailyChallengeRepository;

    @Mock
    private GameService gameService;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DailyChallengeAPIServiceImpl dailyChallengeAPIService;

    private UserEntity user;
    private PictureEntity picture;
    private DailyChallengeEntity dailyChallenge;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.user = new UserEntity();
        this.user.setUsername("testUser");

        this.picture = new PictureEntity();
        this.picture.setId(1L);
        this.picture.setUrl("test.url");
        this.picture.setYear(2024);
        this.picture.setLatitude(42.6977);
        this.picture.setLongitude(23.3219);

        this.dailyChallenge = new DailyChallengeEntity();
        this.dailyChallenge.setPicture(picture);
        this.dailyChallenge.setDate(LocalDate.of(2024,1,1));

        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    }


    @Test
    void testGetCurrentDailyChallengeSuccess() {
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(this.dailyChallenge);
        when(this.modelMapper.map(any(DailyChallengeEntity.class),eq(DailyChallengeDTO.class))).thenReturn(new DailyChallengeDTO());

        DailyChallengeDTO currentChallenge = this.dailyChallengeAPIService.getCurrentChallenge();

        assertNotNull(currentChallenge);
    }

    @Test
    void testGetCurrentDailyChallenge_NotFound() {
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(null);

        assertThrows(DailyChallengeNotFound.class, () -> this.dailyChallengeAPIService.getCurrentChallenge());
    }

    @Test
    void testAddParticipant_Success() {
        ChallengeParticipantEntity challengeParticipantEntity = new ChallengeParticipantEntity();
        challengeParticipantEntity.setUser(user)
        ;
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(this.dailyChallenge);
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(this.user));
        when(this.modelMapper.map(any(ChallengeParticipantDTO.class), eq(ChallengeParticipantEntity.class))).thenReturn(challengeParticipantEntity);

        DailyChallengeDTO dailyChallengeDTO = new DailyChallengeDTO();
        this.dailyChallengeAPIService.addParticipant(dailyChallengeDTO);

        verify(this.dailyChallengeRepository, times(1)).save(any(DailyChallengeEntity.class));
    }

    @Test
    void testSetUserGuess_Success() {
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(this.dailyChallenge);

        ChallengeParticipantEntity challengeParticipantEntity = new ChallengeParticipantEntity();
        challengeParticipantEntity.setUser(this.user);
        this.dailyChallenge.getParticipants().add(challengeParticipantEntity);

        ChallengeParticipantDTO challengeParticipantDTO = new ChallengeParticipantDTO("testUser");

        UserGuessDTO userGuessDTO = new UserGuessDTO();

        this.dailyChallengeAPIService.setUserGuess(challengeParticipantDTO, userGuessDTO);

        verify(this.dailyChallengeRepository, times(1)).save(any(DailyChallengeEntity.class));
    }

    @Test
    void testSetParticipantScore_Success() {
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(this.dailyChallenge);

        ChallengeParticipantEntity challengeParticipantEntity = new ChallengeParticipantEntity();
        challengeParticipantEntity.setUser(this.user);
        this.dailyChallenge.getParticipants().add(challengeParticipantEntity);

        ChallengeParticipantDTO challengeParticipantDTO = new ChallengeParticipantDTO("testUser");

        this.dailyChallengeAPIService.setParticipantScore(challengeParticipantDTO, 10000);
        verify(this.dailyChallengeRepository, times(1)).save(any(DailyChallengeEntity.class));
    }

    @Test
    void testGetForCurrentUser_Success() {
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(this.dailyChallenge);

        ChallengeParticipantEntity challengeParticipantEntity = new ChallengeParticipantEntity();
        challengeParticipantEntity.setUser(this.user);
        challengeParticipantEntity.setScore(10000);
        this.dailyChallenge.getParticipants().add(challengeParticipantEntity);

        when(this.modelMapper.map(any(PictureEntity.class), eq(PictureLocationDTO.class))).thenReturn(new PictureLocationDTO());

        CurrentParticipantDataDTO result = this.dailyChallengeAPIService.getForCurrentUser("testUser");

        assertNotNull(result);
        assertEquals(result.getUsername(), user.getUsername());

    }

    @Test
    void testCurrentGuessDistance_Success() {
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(this.dailyChallenge);

        ChallengeParticipantEntity participant = new ChallengeParticipantEntity();
        participant.setUser(user);
        UserGuessDTO userGuessDTO = new UserGuessDTO();
        participant.setGuess(userGuessDTO);
        this.dailyChallenge.getParticipants().add(participant);

        DailyChallengeDTO dailyChallengeDTO = new DailyChallengeDTO();
        when(this.gameService.getDailyGuessDistance(any(DailyChallengeDTO.class), any(UserGuessDTO.class)))
                .thenReturn(99.9);

        double result = this.dailyChallengeAPIService.currentGuessDistance(dailyChallengeDTO, "testUser");

        assertEquals(result, 99.9);
    }

    @Test
    void testUserAlreadyParticipant_Success() {
        when(this.dailyChallengeRepository.findByDate(any(LocalDate.class))).thenReturn(this.dailyChallenge);

        ChallengeParticipantEntity participant = new ChallengeParticipantEntity();
        participant.setUser(user);
        this.dailyChallenge.getParticipants().add(participant);

        ChallengeParticipantDTO challengeParticipantDTO = this.dailyChallengeAPIService.userAlreadyParticipated();

        assertNotNull(challengeParticipantDTO);
        assertEquals(challengeParticipantDTO.getUsername(), user.getUsername());
    }
}
