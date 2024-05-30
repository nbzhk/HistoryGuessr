package org.softuni.finalproject.service.impl;


import jakarta.servlet.http.HttpSession;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.service.GameService;
import org.softuni.finalproject.service.PictureService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class GameServiceImpl implements GameService {

    private static final String GAME_SESSION_ATTR = "gameSession";
    private static final double EARTH_RADIUS = 6371;
    private static final int MAX_YEAR_DIFFERENCE = 124;
    private static final double MAX_DISTANCE_KM = 20037.5;

    private final PictureService pictureService;


    public GameServiceImpl(PictureService pictureService) {
        this.pictureService = pictureService;
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
        int yearDiff = calculateYearDifference(gameSessionDTO);
        double distanceInKm = calculateDistanceInKm(gameSessionDTO);

        int yearScore = 5000 * (1 - (yearDiff / MAX_YEAR_DIFFERENCE));

        int distanceScore = (int) (5000 * (1 - (distanceInKm / MAX_DISTANCE_KM)));

        int roundScore = yearScore + distanceScore;

        setRoundScore(roundScore, gameSessionDTO);

    }

    private int calculateYearDifference(GameSessionDTO gameSessionDTO) {
        int round = gameSessionDTO.getRound();
        int guessYear = gameSessionDTO.getUserGuesses()[round - 1].getGuessYear();
        int actualYear = gameSessionDTO.getPictureLocations()[round - 1].getYear();
        int yearDifference = Math.abs(guessYear - actualYear);

        setRoundYearDifference(yearDifference, gameSessionDTO);
        return yearDifference;
    }

    private double calculateDistanceInKm(GameSessionDTO gameSessionDTO) {
        int round = gameSessionDTO.getRound();
        double actualLatitude = gameSessionDTO.getPictureLocations()[round - 1].getLatitude();
        double actualLongitude = gameSessionDTO.getPictureLocations()[round - 1].getLongitude();
        Double guessLat = gameSessionDTO.getUserGuesses()[round - 1].getGuessLat();
        Double guessLng = gameSessionDTO.getUserGuesses()[round - 1].getGuessLng();


        return haversineFormula(actualLatitude, actualLongitude, guessLat, guessLng, gameSessionDTO);

    }

    private double haversineFormula(double actualLatitude, double actualLongitude, Double guessLat, Double guessLng, GameSessionDTO gameSessionDTO) {

        double latDistance = Math.toRadians(guessLat - actualLatitude);
        double lngDistance = Math.toRadians(guessLng - actualLongitude);

        // Haversine formula
        double a = Math.pow(Math.sin(latDistance / 2), 2) +
                Math.cos(Math.toRadians(actualLatitude))
                        * Math.cos(Math.toRadians(guessLat))
                        * Math.pow(Math.sin(lngDistance / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        setRoundDistance(distance, gameSessionDTO);
        return distance;
    }

    @Override
    public void setUserGuess(UserGuess userGuess, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setUserGuess(userGuess);

    }


    @Override
    public PictureLocationDTO getCurrentLocation(GameSessionDTO gameSessionDTO) {
        return gameSessionDTO.getPictureLocations()[gameSessionDTO.getRound() - 1];
    }



    @Override
    public GameSessionDTO getCurrentGame(HttpSession session) {
        return (GameSessionDTO) session.getAttribute(GAME_SESSION_ATTR);
    }


    private void setRoundYearDifference(int roundYearDifference, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setRoundYearDifference(roundYearDifference);
    }


    private void setRoundDistance(double roundDistance, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setRoundDistanceDifference(roundDistance);
    }

    private void setRoundScore(int roundScore, GameSessionDTO gameSessionDTO) {
        gameSessionDTO.setRoundScore(roundScore);
    }
}
