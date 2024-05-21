package org.softuni.finalproject.config;

import org.modelmapper.ModelMapper;
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

        modelMapper.addConverter(new UserRoleConverter());
        modelMapper.addConverter(new DropboxCredentialConverter(appKey, appSecret));

        return modelMapper;
    }
}
