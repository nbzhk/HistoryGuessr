package org.softuni.finalproject.service.impl;


import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.dto.*;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.softuni.finalproject.service.GameService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.security.Principal;
import java.util.Optional;

@Service
@SessionScope
public class GameServiceImpl implements GameService {

    private static final String GAME_SESSION_ATTR = "gameSession";
    private static final double EARTH_RADIUS = 6371;
    private static final int MAX_YEAR_DIFFERENCE = 124;
    private static final double MAX_DISTANCE_KM = 20037.5;
    private static final int YEAR_THRESHOLD = 60;
    private static final double DISTANCE_THRESHOLD = 5000;

    private final PictureService pictureService;
    private final DailyChallengeAPIService dailyChallengeAPIService;
    private final Principal principal;


    public GameServiceImpl(PictureService pictureService, DailyChallengeAPIService dailyChallengeAPIService) {
        this.pictureService = pictureService;
        this.dailyChallengeAPIService = dailyChallengeAPIService;

        this.principal = SecurityContextHolder.getContext().getAuthentication();
    }


    @Override
    public GameSessionDTO startGame(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        GameSessionDTO gameSession = new GameSessionDTO(user, pictureLocations());
        session.setAttribute(GAME_SESSION_ATTR, gameSession);
        return gameSession;
    }

    private PictureLocationDTO[] pictureLocations() {
        return this.pictureService.createPictureLocations();
    }


    @Override
    public void calculateRoundScore(GameSessionDTO gameSessionDTO) {
        int round = gameSessionDTO.getRound();
        int yearDiff = calculateYearDifference(gameSessionDTO.getUserGuesses()[round - 1].getGuessYear(),
                gameSessionDTO.getPictureLocations()[round - 1].getYear(),
                gameSessionDTO);

        double distanceInKm = calculateDistanceInKm(gameSessionDTO);

        int roundScore = score(yearDiff, distanceInKm);

        setRoundScore(roundScore, gameSessionDTO);

    }

     int calculateYearDifference(int guessYear, int actualYear, GameSessionDTO gameSessionDTO) {
        int yearDifference = Math.abs(guessYear - actualYear);

        if (gameSessionDTO != null) {
            setRoundYearDifference(yearDifference, gameSessionDTO);
        }
        return yearDifference;
    }

     double calculateDistanceInKm(GameSessionDTO gameSessionDTO) {
        int round = gameSessionDTO.getRound();
        double actualLatitude = gameSessionDTO.getPictureLocations()[round - 1].getLatitude();
        double actualLongitude = gameSessionDTO.getPictureLocations()[round - 1].getLongitude();
        Double guessLat = gameSessionDTO.getUserGuesses()[round - 1].getGuessLat();
        Double guessLng = gameSessionDTO.getUserGuesses()[round - 1].getGuessLng();


        return haversineFormula(actualLatitude, actualLongitude, guessLat, guessLng, gameSessionDTO);

    }

     double haversineFormula(double actualLatitude,
                                    double actualLongitude,
                                    Double guessLat,
                                    Double guessLng,
                                    GameSessionDTO gameSessionDTO) {

        double latDistance = Math.toRadians(guessLat - actualLatitude);
        double lngDistance = Math.toRadians(guessLng - actualLongitude);

        // Haversine formula
        double a = Math.pow(Math.sin(latDistance / 2), 2) +
                Math.cos(Math.toRadians(actualLatitude))
                        * Math.cos(Math.toRadians(guessLat))
                        * Math.pow(Math.sin(lngDistance / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        if (gameSessionDTO != null) {
            setRoundDistance(distance, gameSessionDTO);
        }

        return distance;
    }

    @Override
    public void setUserGuess(UserGuessDTO userGuessDTO, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setUserGuess(userGuessDTO);
    }


    @Override
    public PictureLocationDTO getCurrentLocation(GameSessionDTO gameSessionDTO) {
        return gameSessionDTO.getPictureLocations()[gameSessionDTO.getRound() - 1];
    }


    @Override
    public GameSessionDTO getCurrentGame(HttpSession session) {
        return (GameSessionDTO) session.getAttribute(GAME_SESSION_ATTR);
    }


    @Override
    public void calculateDailyScore(DailyChallengeDTO dailyChallengeDTO) {

        Optional<ChallengeParticipantDTO> currentUser = dailyChallengeDTO.getParticipants()
                .stream()
                .filter(p -> p.getUsername().equals(this.principal.getName()))
                .findFirst();

        if (currentUser.isPresent()) {

            int yearDiff = this.calculateYearDifference(currentUser.get().getUserGuessDTO().getGuessYear(),
                    dailyChallengeDTO.getPicture().getYear(),
                    null);

            double distanceInKm = this.haversineFormula(dailyChallengeDTO.getPicture().getLatitude(),
                    dailyChallengeDTO.getPicture().getLongitude(),
                    currentUser.get().getUserGuessDTO().getGuessLat(),
                    currentUser.get().getUserGuessDTO().getGuessLng(),
                    null);

            int score = score(yearDiff, distanceInKm);

            this.dailyChallengeAPIService.setParticipantScore(currentUser.get(),score);

        }


    }

    private int score(int yearDiff, double distanceInKm) {
        int score = 0;

        if (yearDiff <= YEAR_THRESHOLD) {
            int yearScore = (int) (5000 * Math.pow((1 - (yearDiff / (double)MAX_YEAR_DIFFERENCE)), 2));
            score += yearScore;
        }

        if (distanceInKm <= DISTANCE_THRESHOLD) {
            int distanceScore = (int) (5000 * Math.pow((1 - (distanceInKm / MAX_DISTANCE_KM)), 2));
            score += distanceScore;
        }
        return score;
    }

    @Override
    public void setDailyGuess(UserGuessDTO userGuessDTO, DailyChallengeDTO dailyChallengeDTO) {

        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        if (principal != null) {
            Optional<ChallengeParticipantDTO> participant = dailyChallengeDTO
                    .getParticipants()
                    .stream()
                    .filter(p -> p.getUsername().equals(principal.getName()))
                    .findFirst();

            if (participant.isPresent()) {
                participant.get().setUserGuessDTO(new UserGuessDTO());
                participant.get().getUserGuessDTO().setGuessLat(userGuessDTO.getGuessLat());
                participant.get().getUserGuessDTO().setGuessLng(userGuessDTO.getGuessLng());
                participant.get().getUserGuessDTO().setGuessYear(userGuessDTO.getGuessYear());

                this.dailyChallengeAPIService.setUserGuess(participant.get(), participant.get().getUserGuessDTO());
            }
        }

    }

    @Override
    public double getDailyGuessDistance(DailyChallengeDTO dailyChallengeDTO, UserGuessDTO userGuessDTO) {
        return this.haversineFormula(dailyChallengeDTO.getPicture().getLatitude(),
                dailyChallengeDTO.getPicture().getLongitude(),
                userGuessDTO.getGuessLat(),
                userGuessDTO.getGuessLng(),
                null);
    }


    void setRoundYearDifference(int roundYearDifference, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setRoundYearDifference(roundYearDifference);
    }


    void setRoundDistance(double roundDistance, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setRoundDistanceDifference(roundDistance);
    }

    void setRoundScore(int roundScore, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setRoundScore(roundScore);
    }
}
