package edu.java.controller.exception;

public class DoubleRegistrationException extends RuntimeException {
    public DoubleRegistrationException(String errorMessage) {
        super(errorMessage);
    }
}
