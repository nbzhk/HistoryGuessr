package org.softuni.finalproject.service.impl;

import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));


    }

    private UserDetails map(UserEntity userEntity) {
       return User.withUsername(userEntity.getUsername())
               .password(userEntity.getPassword())
               .authorities(List.of())
               .build();
    }
}
