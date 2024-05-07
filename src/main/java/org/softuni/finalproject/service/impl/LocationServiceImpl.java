package org.softuni.finalproject.service.impl;

import org.modelmapper.ModelMapper;
import org.softuni.finalproject.model.dto.LocationDTO;
import org.softuni.finalproject.model.entity.LocationEntity;
import org.softuni.finalproject.repository.LocationRepository;
import org.softuni.finalproject.service.LocationService;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    public LocationServiceImpl(LocationRepository locationRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Long saveLocation(double latitude, double longitude) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(latitude);
        locationDTO.setLongitude(longitude);

        LocationEntity location = this.modelMapper.map(locationDTO, LocationEntity.class);
        this.locationRepository.save(location);

        return location.getId();
    }

    @Override
    public LocationEntity getLocationById(Long id) {
        return this.locationRepository.getReferenceById(id);
    }

}
