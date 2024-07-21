package org.softuni.finalproject.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PictureRepositoryIT {

    @Autowired
    private PictureRepository pictureRepository;



    @BeforeEach
    public void setUp() {
        pictureRepository.deleteAll();

        PictureEntity pictureEntityOne = new PictureEntity();
        pictureEntityOne.setUrl("test-picture-one");
        pictureEntityOne.setDescription("test-picture-one");
        pictureEntityOne.setYear(2024);
        pictureEntityOne.setLatitude(42.6977);
        pictureEntityOne.setLongitude(23.3219);

        PictureEntity pictureEntityTwo = new PictureEntity();
        pictureEntityTwo.setUrl("test-picture-two");
        pictureEntityTwo.setDescription("test-picture-two");
        pictureEntityTwo.setYear(2024);
        pictureEntityTwo.setLatitude(42.6977);
        pictureEntityTwo.setLongitude(23.3219);

        pictureRepository.save(pictureEntityOne);
        pictureRepository.save(pictureEntityTwo);
    }

    @Test
    public void findByUrlTest() {
        PictureEntity byUrl = this.pictureRepository.findByUrl("test-picture-one");

        assertNotNull(byUrl);
        assertEquals(byUrl.getUrl(), "test-picture-one");
    }

    @Test
    public void findRandomPicturesTest() {
        List<PictureEntity> randomPictures =
                this.pictureRepository.findRandomPictures(1);

        assertEquals(1, randomPictures.size());
    }

    @Test
    public void findRandomDailyPicture() {
        Optional<PictureEntity> randomDailyPicture = this.pictureRepository.findRandomDailyPicture(1);

        assertTrue(randomDailyPicture.isPresent());
    }
}
