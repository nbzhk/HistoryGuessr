package org.softuni.finalproject.Init;

import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.repository.RolesRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup){
            return;
        }
        createAdminRoleIfNotFound();
        createUserRoleIfNotFound();
        createAdminIfNotExist();

        alreadySetup = true;

    }

    private void createAdminIfNotExist() {
        Optional<UserEntity> admin = this.userRepository.findByUsername("admin");

        if (admin.isEmpty()) {
            UserRoleEntity adminRole = rolesRepository.findByUserRole(UserRoleEnum.ADMIN);
            UserRoleEntity userRole = rolesRepository.findByUserRole(UserRoleEnum.USER);
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin@admin.com");
            user.setRegistrationDate(LocalDate.now());
            user.setUserRoles(List.of(adminRole, userRole));

            userRepository.save(user);
        }
    }

    @Transactional
    void createAdminRoleIfNotFound() {
        UserRoleEntity adminRole = rolesRepository.findByUserRole(UserRoleEnum.ADMIN);
        if (adminRole == null) {
            adminRole = new UserRoleEntity();
            adminRole.setUserRole(UserRoleEnum.ADMIN);
            this.rolesRepository.save(adminRole);
        }

    }

    @Transactional
    void createUserRoleIfNotFound() {
        UserRoleEntity userRole = rolesRepository.findByUserRole(UserRoleEnum.USER);
        if (userRole == null) {
            userRole = new UserRoleEntity();
            userRole.setUserRole(UserRoleEnum.USER);
            this.rolesRepository.save(userRole);
        }

    }

}
