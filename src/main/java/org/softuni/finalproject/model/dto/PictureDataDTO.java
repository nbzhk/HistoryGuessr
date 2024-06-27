package org.softuni.finalproject.model.dto;

import jakarta.validation.constraints.*;
import org.softuni.finalproject.validation.picture.DecimalPlaces;
import org.softuni.finalproject.validation.picture.ValidFile;
import org.springframework.web.multipart.MultipartFile;

public class PictureDataDTO {

    @ValidFile
    private MultipartFile picture;
    @NotNull(message = "Latitude is required!")
    @Min(value = -90, message = "Latitude value must be between -90 and 90!")
    @Max(value = 90, message = "Latitude value must be between -90 and 90!")
    @DecimalPlaces
    private Double latitude;
    @NotNull(message = "Longitude is required!")
    @Min(value = -180, message = "Longitude value must be between -180 and 180!")
    @Max(value = 180, message = "Longitude value must be between -180 and 180!")
    @DecimalPlaces
    private Double longitude;
    @NotNull(message = "Year is required!")
    @Min(value = 1820, message = "Year must be after 1820!")
    @Max(value = 2024, message = "Year must be before 2024!")
    private Integer year;
    @NotBlank(message = "Description is required!")
    private String description;

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
