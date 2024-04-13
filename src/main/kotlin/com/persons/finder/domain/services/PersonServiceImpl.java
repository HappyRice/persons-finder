package com.persons.finder.domain.services;

import com.persons.finder.data.dto.PersonDto;
import com.persons.finder.data.model.Person;
import com.persons.finder.data.persistence.PersonRepository;
import com.persons.finder.data.transformer.PersonTransformer;
import com.persons.finder.domain.exception.PersonNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    PersonServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public PersonDto createPerson(final String name) {
        final Person person = Person.builder()
                .withName(name)
                .build();

        this.save(person);

        return PersonTransformer.buildPersonDto(person);
    }

    @Override
    public Person getById(final long id) throws PersonNotFoundException {
        final Person person = this.personRepository.getPersonById(id);

        if (person != null) {
            return person;
        } else {
            throw new PersonNotFoundException();
        }
    }

    @Override
    public void save(final Person person) {
        this.personRepository.persist(person);
    }

    @Override
    public void merge(final Person person) {
        this.personRepository.merge(person);
    }

    @Override
    public List<Person> getEveryoneExceptGivenPerson(final long id) {
        return this.personRepository.getEveryoneExceptGivenPerson(id);
    }

}