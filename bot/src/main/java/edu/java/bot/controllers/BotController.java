package edu.java.bot.controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.services.MessageProcessor;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@Getter
public class BotController {

    private final TelegramBot bot;
    private final MessageProcessor messageProcessor;

    public BotController(TelegramBot bot, MessageProcessor messageProcessor) {
        this.bot = bot;
        this.messageProcessor = messageProcessor;
    }

    @PostConstruct
    private void run() {
        log.info("Bot has started working");

        bot.setUpdatesListener(list -> {
            list.forEach(update -> messageProcessor.checkUpdate(update, bot));

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
