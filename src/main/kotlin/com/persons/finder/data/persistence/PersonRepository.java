package com.persons.finder.data.persistence;

import com.persons.finder.data.model.Person;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepository extends BaseRepository {

    public Optional<Person> getPersonById(final long id) {
        return Optional.ofNullable(this.getById(Person.class, id));
    }

    @SuppressWarnings(value = "unchecked")
    public List<Person> getEveryoneWithALocationExceptGivenPerson(final long id) {
        final Query<?> query = this.getNamedQuery("getEveryoneWithALocationExceptGivenPerson");
        query.setParameter("id", id);

        return (List<Person>) this.list(query);
    }

    public void savePerson(final Person person) {
        this.persist(person);
    }

}