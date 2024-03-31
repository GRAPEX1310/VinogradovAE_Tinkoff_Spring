package edu.java.scrapper.controller.exception;

public class DoubleRegistrationException extends RuntimeException {
    public DoubleRegistrationException(String errorMessage) {
        super(errorMessage);
    }
}
