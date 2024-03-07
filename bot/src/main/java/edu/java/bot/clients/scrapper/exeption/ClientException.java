package edu.java.bot.clients.scrapper.exeption;

import edu.java.dto.response.ClientErrorResponse;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final ClientErrorResponse errorResponse;

    public ClientException(ClientErrorResponse response) {
        errorResponse = response;
    }
}
