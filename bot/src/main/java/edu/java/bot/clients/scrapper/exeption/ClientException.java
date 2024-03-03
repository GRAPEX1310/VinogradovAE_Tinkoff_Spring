package edu.java.bot.clients.scrapper.exeption;

import edu.java.bot.clients.scrapper.dto.ClientErrorResponse;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final ClientErrorResponse errorResponse;

    public ClientException(ClientErrorResponse response) {
        errorResponse = response;
    }
}
