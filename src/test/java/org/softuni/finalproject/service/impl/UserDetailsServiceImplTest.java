package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPass";

    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void testLoadUserByUsername() {

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserRole(UserRoleEnum.USER);

        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setUserRole(UserRoleEnum.ADMIN);

        List<UserRoleEntity> testUserRoles = new ArrayList<>();
        testUserRoles.add(adminRole);
        testUserRoles.add(userRole);

        UserEntity testUser = new UserEntity();
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setUserRoles(testUserRoles);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(TEST_USERNAME);

        Assertions.assertInstanceOf(User.class, userDetails);

        assertEquals(TEST_USERNAME, userDetails.getUsername());
        assertEquals(TEST_PASSWORD, userDetails.getPassword());

        List<String> expected = testUser.getUserRoles()
                .stream()
                .map(UserRoleEntity::getUserRole)
                .map(r -> "ROLE_" + r)
                .toList();

        List<String> actual = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        assertEquals(expected, actual);

    }
}
