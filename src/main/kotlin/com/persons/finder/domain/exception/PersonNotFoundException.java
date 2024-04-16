package com.persons.finder.domain.exception;

public class PersonNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "Person was not found";

    public PersonNotFoundException(final String message) {
        super(message);
    }

    public PersonNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

}
