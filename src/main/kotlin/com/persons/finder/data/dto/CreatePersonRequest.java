package com.persons.finder.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = CreatePersonRequest.Builder.class)
public class CreatePersonRequest {

    @NotNull(message = "Name must not be null")
    @Size(max = 50, message = "Name is too long")
    private final String name;

    public CreatePersonRequest(final Builder builder) {
        this.name = builder.name;
    }

    public String getName() {
        return this.name;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        private String name;

        private Builder() {
            // Prevent Instantiation
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public CreatePersonRequest build() {
            return new CreatePersonRequest(this);
        }
    }

}