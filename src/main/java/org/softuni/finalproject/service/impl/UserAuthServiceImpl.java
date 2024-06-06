package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.LoggedUserDTO;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.UserAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserAuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }

        return null;
    }

    @Override
    public LoggedUserDTO getUserInformation(String username) {

        Optional<UserEntity> user = this.userRepository.findByUsername(username);

        return user.map(userEntity -> this.modelMapper.map(userEntity, LoggedUserDTO.class)).orElse(null);

    }
}
