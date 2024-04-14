package com.persons.finder.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = LocationDto.Builder.class)
public class LocationDto {

    @NotNull(message = "Latitude must not be null")
    @DecimalMax(value = "90", message = "Latitude should not exceed 90")
    @DecimalMin(value = "-90", message = "Latitude should not be less than -90")
    @Digits(integer = 2, fraction = 6, message = "Latitude should not exceed 6 decimal places")
    private final BigDecimal latitude;

    @NotNull(message = "Longitude must not be null")
    @DecimalMax(value = "180", message = "Longitude should not exceed 180")
    @DecimalMin(value = "-180", message = "Longitude should not be less than -180")
    @Digits(integer = 3, fraction = 6, message = "Longitude should not exceed 6 decimal places")
    private final BigDecimal longitude;

    public LocationDto(final Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        private BigDecimal latitude;
        private BigDecimal longitude;

        private Builder() {
            // Prevent Instantiation
        }

        public Builder withLatitude(final BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        public LocationDto build() {
            return new LocationDto(this);
        }
    }

}