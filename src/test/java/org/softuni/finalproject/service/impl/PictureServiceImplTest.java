package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.PictureLocationDTO;
import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.repository.PictureRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class PictureServiceImplTest {

    private static final String TEST_URL = "http://test.url";
    private static final String SECOND_TEST_URL = "http://test.url.second";
    private static final String TEST_DESCRIPTION = "Test description";
    private static final int TEST_YEAR = 2024;
    private static final double TEST_LATITUDE = 51.432;
    private static final double TEST_LONGITUDE = 51.432;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private ModelMapper modelMapper;

    @Spy
    @InjectMocks
    private PictureServiceImpl pictureServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePictureTest() {
        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setUrl(TEST_URL);
        pictureEntity.setDescription(TEST_DESCRIPTION);
        pictureEntity.setYear(TEST_YEAR);
        pictureEntity.setLatitude(TEST_LATITUDE);
        pictureEntity.setLongitude(TEST_LONGITUDE);

        when(modelMapper.map(any(PictureLocationDTO.class), eq(PictureEntity.class))).thenReturn(pictureEntity);

        pictureServiceImpl.savePicture(TEST_URL, TEST_DESCRIPTION, TEST_YEAR, TEST_LATITUDE, TEST_LONGITUDE);

        verify(pictureRepository).save(pictureEntity);

    }

    @Test
    void getCurrentGamePicturesTest() {

        PictureLocationDTO firstPicture = new PictureLocationDTO();
        firstPicture.setUrl(TEST_URL);
        PictureLocationDTO secondPicture = new PictureLocationDTO();
        secondPicture.setUrl(SECOND_TEST_URL);

        PictureEntity firstEntity = new PictureEntity();
        firstEntity.setUrl(TEST_URL);
        PictureEntity secondEntity = new PictureEntity();
        secondEntity.setUrl(SECOND_TEST_URL);

        when(pictureRepository.findByUrl(TEST_URL)).thenReturn(firstEntity);
        when(pictureRepository.findByUrl(SECOND_TEST_URL)).thenReturn(secondEntity);

        PictureLocationDTO[] pictureLocationDTOS = {firstPicture, secondPicture};

        List<PictureEntity> currentGamePictures = pictureServiceImpl.getCurrentGamePictures(pictureLocationDTOS);

        verify(pictureRepository).findByUrl(TEST_URL);
        verify(pictureRepository).findByUrl(SECOND_TEST_URL);

        assertEquals(2, currentGamePictures.size());
        assertEquals(firstEntity, currentGamePictures.get(0));
        assertEquals(secondEntity, currentGamePictures.get(1));

    }

    @Test
    void createPictureLocationsTest() {
        PictureEntity firstEntity = new PictureEntity();
        firstEntity.setUrl(TEST_URL);
        PictureEntity secondEntity = new PictureEntity();
        secondEntity.setUrl(SECOND_TEST_URL);
        PictureEntity thirdEntity = new PictureEntity();
        thirdEntity.setUrl("thirdUrl");
        PictureEntity fourthEntity = new PictureEntity();
        fourthEntity.setUrl("fourthUrl");
        PictureEntity fifthEntity = new PictureEntity();
        fifthEntity.setUrl("fifthUrl");


        PictureLocationDTO firstDTO = new PictureLocationDTO();
        firstDTO.setUrl(TEST_URL);
        PictureLocationDTO secondDTO = new PictureLocationDTO();
        secondDTO.setUrl(SECOND_TEST_URL);
        PictureLocationDTO thirdDTO = new PictureLocationDTO();
        thirdDTO.setUrl("thirdUrl");
        PictureLocationDTO fourthDTO = new PictureLocationDTO();
        fourthDTO.setUrl("fourthUrl");
        PictureLocationDTO fifthDTO = new PictureLocationDTO();
        fifthDTO.setUrl("fifthUrl");

        List<PictureEntity> mockedPictures = Arrays.asList(firstEntity, secondEntity, thirdEntity, fourthEntity, fifthEntity);

        when(pictureRepository.findRandomPictures(5)).thenReturn(mockedPictures);
        when(modelMapper.map(firstEntity, PictureLocationDTO.class)).thenReturn(firstDTO);
        when(modelMapper.map(secondEntity, PictureLocationDTO.class)).thenReturn(secondDTO);
        when(modelMapper.map(thirdEntity, PictureLocationDTO.class)).thenReturn(thirdDTO);
        when(modelMapper.map(fourthEntity, PictureLocationDTO.class)).thenReturn(fourthDTO);
        when(modelMapper.map(fifthEntity, PictureLocationDTO.class)).thenReturn(fifthDTO);

        PictureLocationDTO[] result = pictureServiceImpl.createPictureLocations();
        PictureLocationDTO[] expected = {firstDTO, secondDTO, thirdDTO, fourthDTO, fifthDTO};

        assertArrayEquals(result, expected);

    }
}
