package com.persons.finder.data.transformer;

import com.persons.finder.data.dto.LocationDto;
import com.persons.finder.data.model.Location;
import com.persons.finder.data.model.Person;

import java.math.RoundingMode;

public final class LocationTransformer {

    private LocationTransformer() {
        // Prevent Instantiation
    }

    public static LocationDto buildLocationDto(final Location location) {
        if (location != null) {
            return LocationDto.builder()
                    .withLatitude(location.getLatitude().setScale(6, RoundingMode.UNNECESSARY))
                    .withLongitude(location.getLongitude().setScale(6, RoundingMode.UNNECESSARY))
                    .build();
        } else {
            return null;
        }
    }

    public static Location buildLocation(final LocationDto location, final Person person) {
        return Location.builder()
                .withLatitude(location.getLatitude())
                .withLongitude(location.getLongitude())
                .withPerson(person)
                .build();
    }

}