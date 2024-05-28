package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pictures")
public class PictureEntity extends BaseEntity {

    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private int year;
    @Column
    private String description;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
