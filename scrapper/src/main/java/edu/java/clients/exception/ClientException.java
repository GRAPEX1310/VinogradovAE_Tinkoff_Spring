package edu.java.clients.exception;


import edu.java.clients.bot.dto.ClientErrorResponse;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final ClientErrorResponse errorResponse;

    public ClientException(ClientErrorResponse response) {
        errorResponse = response;
    }
}
