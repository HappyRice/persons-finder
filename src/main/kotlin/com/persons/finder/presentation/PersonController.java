package com.persons.finder.presentation;

import com.persons.finder.data.dto.CreatePersonRequest;
import com.persons.finder.data.dto.ErrorResponse;
import com.persons.finder.data.dto.LocationDto;
import com.persons.finder.data.dto.PersonDto;
import com.persons.finder.data.transformer.PersonTransformer;
import com.persons.finder.domain.exception.PersonNotFoundException;
import com.persons.finder.domain.services.LocationService;
import com.persons.finder.domain.services.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@RestController
@RequestMapping("api/v1/persons")
@Api(tags = "Person")
public class PersonController {

    private final PersonService personService;

    private final LocationService locationService;

    public PersonController(final PersonService personsService, final LocationService locationService) {
        this.personService = personsService;
        this.locationService = locationService;
    }

    @ApiOperation(
            value = "Adds or updates the location for a given person",
            httpMethod = "PUT",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Location added/updated successfully.", response = PersonDto.class),
            @ApiResponse(code = SC_BAD_REQUEST, message = "The request was invalid.", response = ErrorResponse.class),
            @ApiResponse(code = SC_NOT_FOUND, message = "Person was not found.", response = ErrorResponse.class),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An internal server error occurred.", response = ErrorResponse.class)
    })
    @PutMapping(value = "/{personId}/location", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto addOrUpdateLocation(@PathVariable final long personId, @Valid @RequestBody final LocationDto locationDto) throws PersonNotFoundException {

        return this.locationService.addOrUpdateLocation(personId, locationDto);
    }

    @ApiOperation(
            value = "Creates a person",
            httpMethod = "POST",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Person created successfully.", response = PersonDto.class),
            @ApiResponse(code = SC_BAD_REQUEST, message = "The request was invalid.", response = ErrorResponse.class),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An internal server error occurred.", response = ErrorResponse.class)
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto createPerson(@Valid @RequestBody final CreatePersonRequest request) {

        return this.personService.createPerson(request.getName());
    }

    @ApiOperation(
            value = "Fetch list of other people within radius of given person",
            httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_VALUE,
            responseContainer = "List",
            response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "List of other people fetched successfully.", response = PersonDto.class, responseContainer = "List"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "The request was invalid.", response = ErrorResponse.class),
            @ApiResponse(code = SC_NOT_FOUND, message = "Person was not found.", response = ErrorResponse.class),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An internal server error occurred.", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{personId}/vicinity", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDto> getPeopleWithinRadius(@PathVariable final long personId, @RequestParam final double radiusInKm) throws PersonNotFoundException {

        return this.locationService.findAround(personId, radiusInKm);
    }

    @ApiOperation(
            value = "Fetch a person",
            httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Person fetched successfully.", response = PersonDto.class),
            @ApiResponse(code = SC_BAD_REQUEST, message = "The request was invalid.", response = ErrorResponse.class),
            @ApiResponse(code = SC_NOT_FOUND, message = "Person was not found.", response = ErrorResponse.class),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An internal server error occurred.", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto getPerson(@PathVariable final long personId) throws PersonNotFoundException {

        return PersonTransformer.buildPersonDto(this.personService.getById(personId));
    }
}