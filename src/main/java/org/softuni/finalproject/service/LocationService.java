package org.softuni.finalproject.service;

import org.softuni.finalproject.model.dto.LocationDTO;
import org.softuni.finalproject.model.entity.LocationEntity;

public interface LocationService {

    Long saveLocation(double latitude, double longitude);

    LocationEntity getLocationById(Long id);
}
