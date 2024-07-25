package org.softuni.finalproject.service.impl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.service.PictureService;
import org.softuni.finalproject.service.UserService;

public class GameServiceIntegrationTest {

    @Mock
    private GameSessionRepository gameSessionRepository;

    @Mock
    private UserService userService;

    @Mock
    private PictureService pictureService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GameServiceImpl gameServiceImpl;


}