package com.persons.finder.domain.services;

import com.persons.finder.data.dto.PersonDto;
import com.persons.finder.data.model.Person;
import com.persons.finder.domain.exception.PersonNotFoundException;

import java.util.List;

public interface PersonService {
    Person getById(final long id) throws PersonNotFoundException;

    PersonDto createPerson(final String name);

    void save(final Person person);

    List<Person> getEveryoneExceptGivenPerson(final long id);
}