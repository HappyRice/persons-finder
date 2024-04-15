package com.persons.finder.domain.services;

import com.persons.finder.data.dto.LocationDto;
import com.persons.finder.data.dto.PersonDto;
import com.persons.finder.data.model.Location;
import com.persons.finder.data.model.Person;
import com.persons.finder.data.persistence.LocationRepository;
import com.persons.finder.data.transformer.LocationTransformer;
import com.persons.finder.data.transformer.PersonTransformer;
import com.persons.finder.domain.exception.PersonNotFoundException;
import com.persons.finder.domain.utility.DistanceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger LOGGER = LogManager.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final PersonService personService;

    public LocationServiceImpl(final LocationRepository locationRepository, final PersonService personService) {
        this.locationRepository = locationRepository;
        this.personService = personService;
    }

    @Override
    @Transactional
    public PersonDto addOrUpdateLocation(final long personId, final LocationDto locationDto) throws PersonNotFoundException {
        final Person person = this.personService.getById(personId);

        Location location = person.getLocation();

        if (location != null) {
            LOGGER.info("Updating location...");
            person.getLocation().setLatitude(locationDto.getLatitude());
            person.getLocation().setLongitude(locationDto.getLongitude());
        } else {
            LOGGER.info("Creating location...");
            location = LocationTransformer.buildLocation(locationDto, person);
            person.setLocation(location);
            this.locationRepository.saveLocation(location);
        }

        return PersonTransformer.buildPersonDto(person);
    }

    @Override
    @Transactional
    public void removeLocation(final Location location) {
        LOGGER.info("Removing location...");
        this.locationRepository.removeLocation(location);
    }

    @Override
    public List<PersonDto> findAround(final long personId, final double radiusInKm) throws PersonNotFoundException {
        final Person person = this.personService.getById(personId);

        final List<Person> persons = this.personService.getEveryoneWithALocationExceptGivenPerson(personId);

        // Returns everyone in the given vicinity sorted by closest first
        return persons.stream()
                .map(p -> Pair.of(p, DistanceUtil.calculateDistance(person.getLocation().getLatitude(), person.getLocation().getLongitude(),
                p.getLocation().getLatitude(), p.getLocation().getLongitude())))
                .filter(p -> p.getSecond().compareTo(BigDecimal.valueOf(radiusInKm)) < 1)
                .sorted(Comparator.comparing(Pair::getSecond))
                .map(p -> PersonTransformer.buildPersonDto(p.getFirst()))
                .collect(Collectors.toList());
    }

}