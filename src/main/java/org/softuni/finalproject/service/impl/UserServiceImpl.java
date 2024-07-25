package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.*;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.repository.GameSessionRepository;
import org.softuni.finalproject.repository.RolesRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.softuni.finalproject.service.UserService;
import org.softuni.finalproject.service.exception.UserNotFound;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RolesRepository rolesRepository;
    private final GameSessionRepository gameSessionRepository;


    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           RolesRepository rolesRepository,
                           GameSessionRepository gameSessionRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.rolesRepository = rolesRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public boolean register(UserRegistrationDTO userRegistrationDTO) {

        UserEntity user = this.modelMapper.map(userRegistrationDTO, UserEntity.class);

        if (user == null) {
            return false;
        }

        user.setRegistrationDate(LocalDate.now());
        setUserRole(user);
        this.userRepository.save(user);

        return true;
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
        Optional<UserEntity> userToDelete = findByUsername(username);
        userToDelete.ifPresent(this.userRepository::delete);
    }

    @Override
    public void promoteUserToAdmin(String username) {
        Optional<UserEntity> user = findByUsername(username);

        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            UserRoleEntity adminRole = rolesRepository.findByUserRole(UserRoleEnum.ADMIN);
            userEntity.getUserRoles().add(adminRole);

            this.userRepository.save(userEntity);
        }
    }

    @Override
    public void demoteAdminToUser(String username) {
        Optional<UserEntity> user = findByUsername(username);

        if (user.isPresent() && user.get().getUserRoles().get(0).getUserRole().equals(UserRoleEnum.ADMIN)) {
            UserEntity userEntity = user.get();
            userEntity.getUserRoles().remove(rolesRepository.findByUserRole(UserRoleEnum.ADMIN));
            this.userRepository.save(userEntity);
        }
    }

    @Override
    public Map<GameSessionDTO, Integer> getBestGames(String username) {
        Long playerId = getCurrentUserId(username);

        List<Object[]> bestGames = this.gameSessionRepository.findTopFiveGamesForPlayer(playerId);

        return mapData(bestGames);
    }

    private Long getCurrentUserId(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("No such user"));

        return userEntity.getId();
    }

    private Map<GameSessionDTO, Integer> mapData(List<Object[]> byPlayerId) {

        Map<GameSessionDTO, Integer> map = new LinkedHashMap<>();

        for (Object[] result : byPlayerId) {
            GameSessionEntity gameSession = (GameSessionEntity) result[0];
            GameSessionDTO gameSessionDTO = mapToGameSessionDTO(gameSession);
            Integer score = ((Number) result[1]).intValue();
            map.put(gameSessionDTO, score);
        }

        return map;

    }

    private GameSessionDTO mapToGameSessionDTO(GameSessionEntity gameSession) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PictureLocationDTO[] pictures = this.modelMapper.map(gameSession.getPictures(), PictureLocationDTO[].class);
        UserGuessDTO[] userGuessDTOS = this.modelMapper.map(gameSession.getGuesses(), UserGuessDTO[].class);
        int[] scoresArray = gameSession.getRoundScores().stream().mapToInt(Integer::intValue).toArray();
        int[] yearDiffArr = gameSession.getYearDifferences().stream().mapToInt(Integer::intValue).toArray();
        double[] distanceArr = gameSession.getDistanceDifferences().stream().mapToDouble(Double::doubleValue).toArray();

        GameSessionDTO gameSessionDTO = new GameSessionDTO(user, pictures);
        gameSessionDTO.setUserGuesses(userGuessDTOS);
        gameSessionDTO.setRoundScores(scoresArray);
        gameSessionDTO.setYearDifferences(yearDiffArr);
        gameSessionDTO.setDistanceDifferences(distanceArr);
        gameSessionDTO.setTimestamp(gameSession.getTimestamp().toLocalDate());


        return gameSessionDTO;
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

