package com.persons.finder.domain.services;

import com.persons.finder.data.dto.LocationDto;
import com.persons.finder.data.dto.PersonDto;
import com.persons.finder.data.model.Location;
import com.persons.finder.domain.exception.PersonNotFoundException;

import java.util.List;

public interface LocationService {

    PersonDto addOrUpdateLocation(final long personId, final LocationDto location) throws PersonNotFoundException;

    void removeLocation(final Location location);

    List<PersonDto> findAround(final long personId, final double radiusInKm) throws PersonNotFoundException;
}