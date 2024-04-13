package com.persons.finder.data.model;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "Person")
@NamedQuery(name="getEveryoneExceptGivenPerson", query="SELECT person FROM Person as person WHERE id != :id AND deletedDate IS NULL")
public class Person extends BaseModel {

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    @Where(clause = "deletedDate IS NULL")
    private Location location;

    public Person() {
    }

    public Person(final Builder builder) {
        this.name = builder.name;
        this.location = builder.location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private Location location;

        private Builder() {
            // Prevent Instantiation
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withLocation(final Location location) {
            this.location = location;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

}