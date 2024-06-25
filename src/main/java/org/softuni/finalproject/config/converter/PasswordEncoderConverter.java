package org.softuni.finalproject.config.converter;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class PasswordEncoderConverter implements Converter<String, String> {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderConverter() {
        this.passwordEncoder = new Pbkdf2PasswordEncoder();
    }


    @Override
    public String convert(MappingContext<String, String> context) {
        return this.passwordEncoder.encode(context.getSource());
    }
}
