package edu.java.controller;


import edu.java.controller.exception.AddingLinkOneMoreTimeException;
import edu.java.controller.exception.ChatIdNotFoundException;
import edu.java.controller.exception.DoubleRegistrationException;
import edu.java.controller.exception.LinkNotFoundException;
import edu.java.controller.exception.UserNotFoundException;
import edu.java.controller.exception.WrongRequestParametersException;
import edu.java.dto.response.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.controller"})
public class ExceptionHandlerController {

    private static final String CHAT_ID_NOT_FOUND = "Chat id not found";

    @ExceptionHandler(AddingLinkOneMoreTimeException.class)
    public ResponseEntity<ApiErrorResponse> addingLinkOneMoreTimeExceptionHandler(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            "This link is already registered",
            HttpStatus.CONFLICT.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.asList(Arrays.toString(exception.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

    @ExceptionHandler(ChatIdNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> chatIdNotFoundHandler(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            CHAT_ID_NOT_FOUND,
            HttpStatus.NOT_FOUND.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.asList(Arrays.toString(exception.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    @ExceptionHandler(DoubleRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> doubleRegistrationExceptionHandler(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            "This user is already registered",
            HttpStatus.CONFLICT.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.asList(Arrays.toString(exception.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> linkNotFoundExceptionHandler(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            "Link not found in user list",
            HttpStatus.NOT_FOUND.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.asList(Arrays.toString(exception.getStackTrace()).split(", "))
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
            Arrays.asList(Arrays.toString(exception.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
            CHAT_ID_NOT_FOUND,
            HttpStatus.NOT_ACCEPTABLE.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.asList(Arrays.toString(exception.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(apiErrorResponse);
    }
}
