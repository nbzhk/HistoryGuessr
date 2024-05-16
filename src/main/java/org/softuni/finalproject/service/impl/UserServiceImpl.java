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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    //TODO: check role assignment
    private final RolesRepository rolesRepository;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;

        this.rolesRepository = rolesRepository;
    }

    @Override
    public void registerUser(UserRegistrationDTO userRegistrationDTO) {

        UserEntity user = this.modelMapper.map(userRegistrationDTO, UserEntity.class);

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
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
    public void deleteUser(String username) {
        Optional<UserEntity> userToDelete = this.userRepository.findByUsername(username);
        userToDelete.ifPresent(this.userRepository::delete);
    }
}
