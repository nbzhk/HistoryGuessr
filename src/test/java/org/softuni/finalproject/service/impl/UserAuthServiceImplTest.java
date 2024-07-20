package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.LoggedUserDTO;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.exception.UserNotFound;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAuthServiceImplTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_NON_EXISTING_USER = "nonExistingUser";

    @Mock
    private UserRepository userRepository;


    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;


    private UserAuthServiceImpl userAuthServiceImpl;

    @BeforeEach
    public void setUp() {
        userAuthServiceImpl = new UserAuthServiceImpl(userRepository, modelMapper);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetCurrentUsername() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(TEST_USERNAME);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        String username = userAuthServiceImpl.getCurrentUsername();

        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void testGetCurrentUsernameThrowsUserNotFound() {

        when(securityContext.getAuthentication()).thenReturn(authentication);

        assertThrows(UserNotFound.class, () -> userAuthServiceImpl.getCurrentUsername());
    }

    @Test
    void testGetUserInformation() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(TEST_USERNAME);

        LoggedUserDTO loggedUserDTO = new LoggedUserDTO();
        loggedUserDTO.setUsername(TEST_USERNAME);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, LoggedUserDTO.class)).thenReturn(loggedUserDTO);

        LoggedUserDTO userInformation = userAuthServiceImpl.getUserInformation(TEST_USERNAME);

        assertNotNull(userInformation);
        assertEquals(TEST_USERNAME, userInformation.getUsername());
    }

    @Test
    void testGetUserInformationUserNotFound() {
        when(userRepository.findByUsername(TEST_NON_EXISTING_USER)).thenReturn(Optional.empty());

        LoggedUserDTO result = userAuthServiceImpl.getUserInformation(TEST_NON_EXISTING_USER);

        assertNull(result);
    }

}
