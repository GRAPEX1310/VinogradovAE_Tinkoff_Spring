package edu.java.bot.controllers;

import edu.java.dto.request.LinkUpdateRequest;
import edu.java.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkUpdateController {

    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Обновление обработано",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation
                                             = String.class))}),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation
                                             = ApiErrorResponse.class))})
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/updates")
    public ResponseEntity<Void> updateProcess(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        return ResponseEntity.ok().build();
    }
}
