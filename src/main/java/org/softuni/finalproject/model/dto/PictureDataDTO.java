package org.softuni.finalproject.model.dto;

import jakarta.validation.constraints.*;
import org.softuni.finalproject.validation.picture.DecimalPlaces;
import org.softuni.finalproject.validation.picture.ValidFile;
import org.springframework.web.multipart.MultipartFile;

public class PictureDataDTO {

    @ValidFile
    @NotNull(message = "{upload.file.null.error}")
    private MultipartFile picture;
    @NotNull(message = "{upload.latitude.null.error}")
    @Min(value = -90, message = "{upload.latitude.value.error}")
    @Max(value = 90, message = "{upload.latitude.value.error}")
    @DecimalPlaces
    private Double latitude;
    @NotNull(message = "{upload.longitude.null.error}")
    @Min(value = -180, message = "{upload.longitude.value.error}")
    @Max(value = 180, message = "{upload.longitude.value.error}")
    @DecimalPlaces
    private Double longitude;
    @NotNull(message = "{upload.year.null.error}")
    @Min(value = 1820, message = "{upload.year.after.error}")
    @Max(value = 2024, message = "{upload.year.before.error}")
    private Integer year;
    @NotBlank(message = "{upload.description.error}")
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
