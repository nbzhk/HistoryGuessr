package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserRegistrationDTO userRegistrationDTO) {

        UserEntity user = this.modelMapper.map(userRegistrationDTO, UserEntity.class);

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setRegistrationDate(LocalDate.now());

        this.userRepository.save(user);

        System.out.println("Saved user: " + user);

    }
}
