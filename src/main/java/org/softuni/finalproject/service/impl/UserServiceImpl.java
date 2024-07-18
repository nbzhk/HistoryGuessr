package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.UserInfoDTO;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.repository.RolesRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RolesRepository rolesRepository;


    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void register(UserRegistrationDTO userRegistrationDTO) {

        UserEntity user = this.modelMapper.map(userRegistrationDTO, UserEntity.class);

        user.setRegistrationDate(LocalDate.now());

        setUserRole(user);

        this.userRepository.save(user);

        System.out.println("Saved user: " + user);

    }

    private void setUserRole(UserEntity user) {
        UserRoleEntity userRole = this.rolesRepository.findByUserRole(UserRoleEnum.USER);
        user.setUserRoles(List.of(userRole));
    }

    @Override
    public List<UserInfoDTO> getAllUsers() {

        return this.userRepository.findAll()
                .stream()
                .map(u -> this.modelMapper.map(u, UserInfoDTO.class))
                .toList();

    }

    @Override
    public void delete(String username) {
        Optional<UserEntity> userToDelete = this.userRepository.findByUsername(username);
        userToDelete.ifPresent(this.userRepository::delete);
    }

    @Override
    public void promoteUserToAdmin(String username) {
        Optional<UserEntity> user = this.userRepository.findByUsername(username);

        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            UserRoleEntity adminRole = rolesRepository.findByUserRole(UserRoleEnum.ADMIN);
            userEntity.getUserRoles().add(adminRole);

            this.userRepository.save(userEntity);
        }
    }

    @Override
    public void demoteAdminToUser(String username) {
        Optional<UserEntity> user = this.userRepository.findByUsername(username);

        if (user.isPresent() && user.get().getUserRoles().get(0).getUserRole().equals(UserRoleEnum.ADMIN)) {
            UserEntity userEntity = user.get();
            userEntity.getUserRoles().remove(rolesRepository.findByUserRole(UserRoleEnum.ADMIN));
            this.userRepository.save(userEntity);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String currentUsername) {
        return this.userRepository.findByUsername(currentUsername);
    }

    @Override
    public void saveUser(UserEntity userEntity) {
        this.userRepository.save(userEntity);
    }
}

