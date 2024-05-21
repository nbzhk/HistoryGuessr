package org.softuni.finalproject.config;

import com.dropbox.core.oauth.DbxCredential;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.softuni.finalproject.model.entity.DropboxCredentialEntity;

public class DropboxCredentialConverter implements Converter<DropboxCredentialEntity, DbxCredential> {


    private final String appKey;
    private final String appSecret;

    public DropboxCredentialConverter(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    @Override
    public DbxCredential convert(MappingContext<DropboxCredentialEntity, DbxCredential> context) {
        DropboxCredentialEntity source = context.getSource();
        return new DbxCredential(
                source.getAccessToken(),
                source.getExpiresAt(),
                source.getRefreshToken(),
                appKey,
                appSecret
        );
    }
}
