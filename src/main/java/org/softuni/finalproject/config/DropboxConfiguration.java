package org.softuni.finalproject.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.SharedLink;
import com.dropbox.core.v2.sharing.RequestedVisibility;
import com.dropbox.core.v2.sharing.SharedLinkSettings;
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
    public DbxClientV2 dbxClient() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(appKey).build();
        return new DbxClientV2(config, appSecret);
    }

    //TODO:Settings
    @Bean
    public SharedLinkSettings settings() {
        return new SharedLinkSettings();
    }
}
