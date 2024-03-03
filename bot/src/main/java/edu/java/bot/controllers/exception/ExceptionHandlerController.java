package edu.java.bot.controllers.exception;

import edu.java.bot.controllers.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.bot.controller"})
public class ExceptionHandlerController {

    @ExceptionHandler(ChatIdNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> chatIdNotFoundHandler(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            "Chat id not found",
            HttpStatus.NOT_FOUND.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    @ExceptionHandler(WrongRequestParametersException.class)
    public ResponseEntity<ApiErrorResponse> wrongRequestParametersHandler(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            "Wrong request parameters",
            HttpStatus.BAD_REQUEST.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }
}
