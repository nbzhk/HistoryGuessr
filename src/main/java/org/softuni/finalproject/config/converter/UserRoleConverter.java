package org.softuni.finalproject.config.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;

import java.util.ArrayList;
import java.util.List;

public class UserRoleConverter implements Converter<List<UserRoleEntity>, List<UserRoleEnum>> {

    @Override
    public List<UserRoleEnum> convert(MappingContext<List<UserRoleEntity>, List<UserRoleEnum>> context) {
        List<UserRoleEnum> result = new ArrayList<>();
        for (UserRoleEntity entity : context.getSource()) {
            result.add(entity.getUserRole());
        }

        return result;
    }
}
