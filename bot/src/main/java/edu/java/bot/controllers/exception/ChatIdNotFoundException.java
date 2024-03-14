package edu.java.bot.controllers.exception;

public class ChatIdNotFoundException extends RuntimeException {
    public ChatIdNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
