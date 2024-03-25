package edu.java.scrapper.controller.exception;

import lombok.Getter;

@Getter
public class WrongUserStateException extends RuntimeException {
    private final Long id;

    public WrongUserStateException(Long userId) {
        id = userId;
    }
}
