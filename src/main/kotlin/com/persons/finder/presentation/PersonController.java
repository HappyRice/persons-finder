package com.persons.finder.presentation;

import com.persons.finder.data.dto.CreatePersonRequest;
import com.persons.finder.data.dto.ErrorResponse;
import com.persons.finder.data.dto.LocationDto;
import com.persons.finder.data.transformer.PersonTransformer;
import com.persons.finder.domain.exception.PersonNotFoundException;
import com.persons.finder.domain.services.LocationService;
import com.persons.finder.domain.services.PersonService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/persons")
public class PersonController {

    private final PersonService personService;

    private final LocationService locationService;

    public PersonController(final PersonService personsService, final LocationService locationService) {
        this.personService = personsService;
        this.locationService = locationService;
    }

    @PutMapping(value = "/{personId}/location", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object addOrUpdateLocation(@PathVariable final long personId, @Valid @RequestBody final LocationDto locationDto,
                                                    final HttpServletResponse response) {
        try {
            return this.locationService.addOrUpdateLocation(personId, locationDto);
        } catch (final PersonNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ErrorResponse.builder()
                    .withSuccess(false)
                    .withMessage(e.getMessage())
                    .build();
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createPerson(@Valid @RequestBody final CreatePersonRequest request) {

        return this.personService.createPerson(request.getName());
    }

    @GetMapping(value = "/{personId}/vicinity", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object getPeopleWithinRadius(@PathVariable final long personId, @RequestParam final double radiusInKm,
                                                      final HttpServletResponse response) {
        try {
            return this.locationService.findAround(personId, radiusInKm);
        } catch (final PersonNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ErrorResponse.builder()
                    .withSuccess(false)
                    .withMessage(e.getMessage())
                    .build();
        }
    }
    @GetMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object getPerson(@PathVariable final long personId, final HttpServletResponse response) {
        try {
            return PersonTransformer.buildPersonDto(this.personService.getById(personId));
        } catch (final PersonNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ErrorResponse.builder()
                    .withSuccess(false)
                    .withMessage(e.getMessage())
                    .build();
        }
    }
}