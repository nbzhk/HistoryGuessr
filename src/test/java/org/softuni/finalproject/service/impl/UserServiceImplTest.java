package org.softuni.finalproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.UserInfoDTO;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.repository.RolesRepository;
import org.softuni.finalproject.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RolesRepository rolesRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("test_username");
        userRegistrationDTO.setPassword("test_password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("test_username");
        userEntity.setRegistrationDate(LocalDate.now());

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUserRole(UserRoleEnum.USER);

        when(modelMapper.map(userRegistrationDTO, UserEntity.class)).thenReturn(userEntity);
        when(rolesRepository.findByUserRole(UserRoleEnum.USER)).thenReturn(userRoleEntity);

        boolean result = userServiceImpl.register(userRegistrationDTO);

        assertTrue(result, "Registration succeed");

        UserRegistrationDTO nullUser = new UserRegistrationDTO();
        nullUser.setUsername(null);
        nullUser.setPassword(null);

        boolean nullUserRegistration = this.userServiceImpl.register(nullUser);

        assertFalse(nullUserRegistration, "Registration should fail");
    }

    @Test
    void getAllUsersTest(){
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");

        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setUsername(user1.getUsername());
        UserInfoDTO userInfoDTO2 = new UserInfoDTO();
        userInfoDTO2.setUsername(user2.getUsername());

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(modelMapper.map(user1, UserInfoDTO.class)).thenReturn(userInfoDTO1);
        when(modelMapper.map(user2, UserInfoDTO.class)).thenReturn(userInfoDTO2);

        List<UserInfoDTO> allUsers = userServiceImpl.getAllUsers();

        assertEquals(2, allUsers.size());
        assertEquals("user1", allUsers.get(0).getUsername());
        assertEquals("user2", allUsers.get(1).getUsername());
    }

    @Test
    void deleteUserTest() {
        String username = "deleteUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        userServiceImpl.delete(username);

        verify(userRepository).delete(userEntity);
    }

    @Test
    void promoteUserToAdminTest() {
        String username = "promoteUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        UserRoleEntity adminRoleEntity = new UserRoleEntity();
        adminRoleEntity.setUserRole(UserRoleEnum.ADMIN);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(rolesRepository.findByUserRole(UserRoleEnum.ADMIN)).thenReturn(adminRoleEntity);

        userServiceImpl.promoteUserToAdmin(username);

        assertEquals(UserRoleEnum.ADMIN, userEntity.getUserRoles().get(0).getUserRole());

        verify(userRepository).save(userEntity);

    }

    @Test
    void demoteAdminToUser() {
        String username = "demoteAdmin";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        UserRoleEntity adminRoleEntity = new UserRoleEntity();
        adminRoleEntity.setUserRole(UserRoleEnum.ADMIN);

        userEntity.setUserRoles(new ArrayList<>());
        userEntity.getUserRoles().add(adminRoleEntity);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(rolesRepository.findByUserRole(UserRoleEnum.ADMIN)).thenReturn(adminRoleEntity);

        userServiceImpl.demoteAdminToUser(username);

        assertEquals(0, userEntity.getUserRoles().size());

        verify(userRepository).save(userEntity);
    }

    @Test
    void findByUsernameTest() {
        String username = "findByUsername";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> foundUser = userServiceImpl.findByUsername(username);

        assertEquals(foundUser.get().getUsername(), username);
    }

    @Test
    void saveUserTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        userServiceImpl.saveUser(userEntity);

        verify(userRepository).save(userEntity);
    }

}
