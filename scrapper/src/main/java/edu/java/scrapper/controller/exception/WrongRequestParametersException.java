package edu.java.scrapper.controller.exception;

public class WrongRequestParametersException extends RuntimeException {
    public WrongRequestParametersException(String errorMessage) {
        super(errorMessage);
    }

}
