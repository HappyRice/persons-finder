package com.persons.finder.data.transformer;

import com.persons.finder.data.dto.PersonDto;
import com.persons.finder.data.model.Person;

public final class PersonTransformer {

    private PersonTransformer() {
        // Prevent Instantiation
    }

    public static PersonDto buildPersonDto(final Person person) {
        return PersonDto.builder()
                .withId(person.getId())
                .withName(person.getName())
                .withLocation(LocationTransformer.buildLocationDto(person.getLocation()))
                .build();
    }

}