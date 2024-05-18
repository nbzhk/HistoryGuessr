package org.softuni.finalproject.config;

import com.dropbox.core.DbxRequestConfig;
import org.softuni.finalproject.service.impl.DropboxAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfiguration {

    @Value("${dropbox.appKey}")
    private String appKey;

    //TODO:change to token
    @Value("${dropbox.appSecret}")
    private String appSecret;


    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        return DbxRequestConfig.newBuilder(appKey).build();
    }

    @Bean
    public DropboxAuthService dropboxAuthService() {
        return new DropboxAuthService(dbxRequestConfig(), appKey, appSecret);
    }
}
