package edu.java.controller.exception;

public class ChatIdNotFoundException extends RuntimeException {
    public ChatIdNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
