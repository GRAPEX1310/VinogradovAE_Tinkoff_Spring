package edu.java.bot.controllers;

import edu.java.dto.request.LinkUpdateRequest;
import edu.java.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class LinkUpdateController {
    private final BotController bot;

    @Autowired
    public LinkUpdateController(BotController botController) {
        bot = botController;
    }

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
    @PostMapping
    public void updateProcess(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        String message = linkUpdateRequest.description() + "\n" + linkUpdateRequest.url();
        for (Long chatId : linkUpdateRequest.tgChatIds()) {
            bot.sendUpdateMessage(chatId, message);
        }
    }
}
