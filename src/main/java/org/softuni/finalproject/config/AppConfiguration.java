package org.softuni.finalproject.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.softuni.finalproject.config.converter.PasswordEncoderConverter;
import org.softuni.finalproject.config.converter.ScoreConverter;
import org.softuni.finalproject.config.converter.UserRoleConverter;
import org.softuni.finalproject.model.dto.ChallengeParticipantDTO;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.dto.UserInfoDTO;
import org.softuni.finalproject.model.dto.UserRegistrationDTO;
import org.softuni.finalproject.model.entity.ChallengeParticipantEntity;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfiguration {

    @Value("${dropbox.appKey}")
    private String appKey;
    @Value("${dropbox.appSecret}")
    private String appSecret;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        /* User roles converter */
        modelMapper.typeMap(UserEntity.class, UserInfoDTO.class)
                .addMappings(mapping -> mapping.using(new UserRoleConverter())
                .map(UserEntity::getUserRoles, UserInfoDTO::setUserRoles));

        /* Dropbox credentials */
        modelMapper.addConverter(new DropboxCredentialConverter(appKey, appSecret));

        /* Round Scores TypeMap */
        TypeMap<GameSessionDTO, GameSessionEntity> typeMap = modelMapper.typeMap(GameSessionDTO.class, GameSessionEntity.class);
        typeMap.addMapping(GameSessionDTO::getScores, GameSessionEntity::setRoundScores);

        /* Score converter */
        modelMapper.addConverter(new ScoreConverter());

        /* Type Map for password encoding */
        modelMapper.createTypeMap(UserRegistrationDTO.class, UserEntity.class)
                .addMappings(mapping -> mapping.using(new PasswordEncoderConverter())
                .map(UserRegistrationDTO::getPassword, UserEntity::setPassword));

        /* Type Map for ChallengeParticipantEntity to DTO*/
        modelMapper.createTypeMap(ChallengeParticipantEntity.class, ChallengeParticipantDTO.class)
                .addMapping(p -> p.getUser().getUsername(), ChallengeParticipantDTO::setUsername);


        return modelMapper;
    }


}
