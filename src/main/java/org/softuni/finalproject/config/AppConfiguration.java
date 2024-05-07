package org.softuni.finalproject.config;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.PictureLocation;
import org.softuni.finalproject.model.dto.PictureDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }
}
