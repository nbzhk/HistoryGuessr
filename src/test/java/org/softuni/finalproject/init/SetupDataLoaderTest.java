package org.softuni.finalproject.init;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.softuni.finalproject.Init.SetupDataLoader;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.repository.RolesRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {SetupDataLoader.class})
public class SetupDataLoaderTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SetupDataLoader setupDataLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createAdminIfNotExistTest() {
        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setUserRole(UserRoleEnum.ADMIN);
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserRole(UserRoleEnum.USER);

        when(this.userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(this.rolesRepository.findByUserRole(UserRoleEnum.ADMIN)).thenReturn(adminRole);
        when(this.rolesRepository.findByUserRole(UserRoleEnum.USER)).thenReturn(userRole);
        when(this.passwordEncoder.encode("admin")).thenReturn("encodedAdminPass");

        this.setupDataLoader.createAdminIfNotExist();

        verify(this.userRepository).save(argThat(user ->
                user.getUsername().equals("admin") &&
                        user.getPassword().equals("encodedAdminPass") &&
                        user.getEmail().equals("admin@admin.com") &&
                        user.getUserRoles().contains(adminRole) &&
                        user.getUserRoles().contains(userRole)
        ));
    }
}
