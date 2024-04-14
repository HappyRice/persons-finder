package com.persons.finder.domain.services;

import com.persons.finder.data.dto.LocationDto;
import com.persons.finder.data.dto.PersonDto;
import com.persons.finder.data.model.Location;
import com.persons.finder.domain.exception.PersonNotFoundException;

import java.util.List;

public interface LocationService {

    PersonDto addOrUpdateLocation(final long personId, final LocationDto location) throws PersonNotFoundException;

    void removeLocation(final Location location);

    /**
     * Finds a list of people around a given person within a given radius
     * @param personId - the reference person
     * @param radiusInKm - mandatory parameter for radius to search within in kilometers
     * @return a list of people found otherwise empty list if no one found
     * @throws PersonNotFoundException - reference person not found with given id
     */
    List<PersonDto> findAround(final long personId, final double radiusInKm) throws PersonNotFoundException;
}