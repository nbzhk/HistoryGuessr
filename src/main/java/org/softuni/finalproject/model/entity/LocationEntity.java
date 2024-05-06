package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class LocationEntity extends BaseEntity {

    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
