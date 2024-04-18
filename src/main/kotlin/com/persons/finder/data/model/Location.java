package com.persons.finder.data.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Location")
public class Location extends BaseModel {

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @OneToOne
    @JoinColumn(name = "personId")
    private Person person;

    public Location() {
    }

    public Location(final Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.person = builder.person;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public void setLatitude(final BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public void setLongitude(final BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(final Person person) {
        this.person = person;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Person person;

        private Builder() {
            // Prevent Instantiation
        }

        public Builder withLatitude(final BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }


        public Builder withPerson(final Person person) {
            this.person = person;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }

}