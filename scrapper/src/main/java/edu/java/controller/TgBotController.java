package edu.java.controller;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.model.User;
import edu.java.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class TgBotController {
    private final UserService userService;

    @Autowired
    public TgBotController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation
                                             = String.class))}),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation
                                             = ApiErrorResponse.class))})
    })
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registerChat(@PathVariable Long id) {
        userService.addUser(new User(id));
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удален",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation
                                             = String.class))}),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation
                                             = ApiErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Чат не найден",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation
                                             = ApiErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteChat(@PathVariable Long id) {
        userService.removeUser(id);
    }
}
