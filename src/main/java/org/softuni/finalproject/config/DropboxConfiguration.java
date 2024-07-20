package org.softuni.finalproject.config;

import com.dropbox.core.DbxRequestConfig;
import org.softuni.finalproject.service.DropboxAuthService;
import org.softuni.finalproject.service.impl.DropboxAuthServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfiguration {

    @Value("${dropbox.appKey}")
    private String appKey;

    @Value("${dropbox.appSecret}")
    private String appSecret;


    @Bean
    public String appKey() {
        return appKey;
    }

    @Bean
    public String appSecret() {
        return appSecret;
    }


    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        return DbxRequestConfig.newBuilder("HistoryGuessr/1.0").build();
    }


    @Bean
    public DropboxAuthService dropboxAuthService() {
        return new DropboxAuthServiceImpl(dbxRequestConfig(),appKey, appSecret);
    }
}
