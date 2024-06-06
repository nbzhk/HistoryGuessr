package org.softuni.finalproject.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.softuni.finalproject.model.dto.GameSessionDTO;
import org.softuni.finalproject.model.entity.GameSessionEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfiguration {

    @Value("${google.map.key}")
    private String googleMapKey;
    @Value("${dropbox.appKey}")
    private String appKey;
    @Value("${dropbox.appSecret}")
    private String appSecret;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new UserRoleConverter());
        modelMapper.addConverter(new DropboxCredentialConverter(appKey, appSecret));

        TypeMap<GameSessionDTO, GameSessionEntity> typeMap = modelMapper.typeMap(GameSessionDTO.class, GameSessionEntity.class);
        typeMap.addMapping(GameSessionDTO::getScores, GameSessionEntity::setRoundScores);

        modelMapper.addConverter(new ScoreConverter());

        return modelMapper;
    }


}
