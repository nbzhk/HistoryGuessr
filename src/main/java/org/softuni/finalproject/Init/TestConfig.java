package org.softuni.finalproject.Init;

import org.softuni.finalproject.model.CurrentUser;
import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.UserGuess;
import org.softuni.finalproject.model.dto.CurrentGame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public CurrentGame currentGame() {
        CurrentUser currentUser = currentUser();
        PictureLocation pictureLocation = pictureLocation();
        UserGuess userGuess = userGuess();

        CurrentGame currentGame = new CurrentGame();
        currentGame.setUser(currentUser);
        currentGame.setPictureLocation(pictureLocation);
        currentGame.setUserGuess(userGuess);

        return currentGame;
    }

    public CurrentUser currentUser() {
        CurrentUser currentUser = new CurrentUser();
        currentUser.setUsername("test");
        return currentUser;
    }

    private UserGuess userGuess() {
        return new UserGuess();
    }


    public PictureLocation pictureLocation() {
        PictureLocation pictureLocation = new PictureLocation();
        pictureLocation.setImgUrl("C:\\Users\\Nikolay Bozhkov\\IdeaProjects\\demo\\src\\main\\resources\\static\\img\\Eiffel.jpeg");
        pictureLocation.setLatitude(48.858093);
        pictureLocation.setLongitude(2.294694);
        pictureLocation.setYear(2024);

        return pictureLocation;
    }
}
