package com.persons.finder.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = PersonDto.Builder.class)
public class PersonDto {

    private final Long id;

    private final String name;

    private final LocationDto location;

    public PersonDto(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.location = builder.location;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocationDto getLocation() {
        return this.location;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        private Long id;
        private String name;
        private LocationDto location;

        private Builder() {
            // Prevent Instantiation
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withLocation(final LocationDto location) {
            this.location = location;
            return this;
        }

        public PersonDto build() {
            return new PersonDto(this);
        }
    }

}