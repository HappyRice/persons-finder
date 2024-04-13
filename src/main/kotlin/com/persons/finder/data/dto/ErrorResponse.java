package com.persons.finder.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = ErrorResponse.Builder.class)
public class ErrorResponse {

    private final Boolean success;

    private final String message;

    public ErrorResponse(final Builder builder) {
        this.success = builder.success;
        this.message = builder.message;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        private Boolean success;
        private String message;

        private Builder() {
            // Prevent Instantiation
        }

        public Builder withSuccess(final Boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(final String message) {
            this.message = message;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }

}