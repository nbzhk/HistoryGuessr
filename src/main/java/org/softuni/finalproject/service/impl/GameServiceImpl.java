package org.softuni.finalproject.service.impl;


import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.GameDTO;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.service.GameService;
import org.softuni.finalproject.service.GameSessionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private static final double EARTH_RADIUS = 6371;
    private static final int MAX_YEAR_DIFFERENCE = 124;
    private static final double MAX_DISTANCE_KM = 20037.5;

    private GameDTO gameDTO;
    private final PictureRepository pictureRepository;
    private final GameSessionService gameSessionService;
    private int roundYearDifference;
    private double roundDistance;


    public GameServiceImpl(PictureRepository pictureRepository, GameSessionService gameSessionService) {
        this.pictureRepository = pictureRepository;
        this.gameSessionService = gameSessionService;
    }

    @Override
    public GameDTO startGame() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return this.gameDTO = new GameDTO(user, pictureLocations());
    }


    //TODO: set the locations from the data base, this is just a test

    private PictureLocationDTO[] pictureLocations() {
        PictureEntity picture = this.pictureRepository.getReferenceById(Long.parseLong("1"));

        PictureLocationDTO[] pictureLocationDTOS = new PictureLocationDTO[5];
        for (int i = 0; i < pictureLocationDTOS.length; i++) {

            PictureLocationDTO pictureLocationDTO = new PictureLocationDTO();
            pictureLocationDTO.setImgUrl(picture.getUrl());
            pictureLocationDTO.setLatitude(picture.getLatitude());
            pictureLocationDTO.setLongitude(picture.getLongitude());
            pictureLocationDTO.setYear(picture.getYear());
            pictureLocationDTO.setDescription(picture.getDescription());

            pictureLocationDTOS[i] = pictureLocationDTO;
        }

        return pictureLocationDTOS;
    }

    @Override
    public void setUserGuess(UserGuess userGuess) {
        this.gameDTO.setUserGuess(userGuess);

    }

    @Override
    public UserGuess getUserGuess() {
        return this.gameDTO.getUserGuesses()[this.gameDTO.getRound() - 1];
    }

    @Override
    public GameDTO getGameSession() {
        return this.gameDTO;
    }

    @Override
    public PictureLocationDTO getCurrentLocation() {
        return this.gameDTO.getPictureLocations()[this.gameDTO.getRound() - 1];
    }

    @Override
    public void calculateResult() {
        int roundScore = 0;
        int yearDiff = calculateYearDifference(this.gameDTO.getRound());

        Double distanceInKm = calculateDistanceInKm(this.gameDTO.getRound());

        double yearRatio = (double) yearDiff / MAX_YEAR_DIFFERENCE;
        roundScore += (int) (2500 * (1- yearRatio));

        if (distanceInKm != null) {
            double distanceRatio =  distanceInKm / MAX_DISTANCE_KM;
            int locationScore = (int) (2500 * (1 - distanceRatio));
            roundScore += locationScore;

        }


        this.gameDTO.addRoundScore(roundScore);
        System.out.println("ROUND SCORE: " + roundScore);
    }


    private int calculateYearDifference(int round) {

        int guessYear = this.gameDTO.getUserGuesses()[round - 1].getGuessYear();
        int actualYear = this.gameDTO.getPictureLocations()[round - 1].getYear();
        this.setRoundYearDifference(Math.abs(guessYear - actualYear));
        return Math.abs(guessYear - actualYear);
    }

    private Double calculateDistanceInKm(int round) {
        double actualLatitude = this.gameDTO.getPictureLocations()[round - 1].getLatitude();
        double actualLongitude = this.gameDTO.getPictureLocations()[round - 1].getLongitude();
        Double guessLat = this.gameDTO.getUserGuesses()[round - 1].getGuessLat();
        Double guessLng = this.gameDTO.getUserGuesses()[round - 1].getGuessLng();

        if (guessLat != null && guessLng != null) {
            double actualLatRad = Math.toRadians(actualLatitude);
            double actualLngRad = Math.toRadians(actualLongitude);
            double guessLatRad = Math.toRadians(guessLat);
            double guessLngRad = Math.toRadians(guessLng);

            double diffLat = actualLatRad - guessLatRad;
            double diffLng = actualLngRad - guessLngRad;

            // Haversine formula
            double a = Math.pow(Math.sin(diffLat / 2), 2) +
                    Math.cos(actualLatRad) * Math.cos(guessLatRad) *
                            Math.pow(Math.sin(diffLng / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            this.setRoundDistance(EARTH_RADIUS * c);
            return EARTH_RADIUS * c;
        }

        return null;
    }

    @Override
    public void nextRound() {
        this.gameDTO.nextRound();
    }

    @Override
    public boolean lastRound() {
        return this.gameDTO.getRound() == 5;
    }

    @Override
    public int getRoundYearDifference() {
        return roundYearDifference;
    }

    public void setRoundYearDifference(int roundYearDifference) {
        this.roundYearDifference = roundYearDifference;
    }

    @Override
    public double getRoundDistance() {
        return roundDistance;
    }

    @Override
    public void saveSession(GameDTO gameDTO) {
        this.gameSessionService.saveGameSession(this.gameDTO);
        this.gameDTO = null;
    }

    public void setRoundDistance(double roundDistance) {
        this.roundDistance = roundDistance;
    }
}
