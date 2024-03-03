package edu.java.bot.controllers.exception;

public class WrongRequestParametersException extends RuntimeException {
    public WrongRequestParametersException(String errorMessage) {
        super(errorMessage);
    }

}
