package edu.java.bot.Controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.Services.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class BotRunner {

    private final TelegramBot bot;
    private final MessageProcessor messageProcessor;

    public BotRunner(TelegramBot bot, MessageProcessor messageProcessor) {
        this.bot = bot;
        this.messageProcessor = messageProcessor;
        this.run();
    }

    private void run() {
        log.info("Bot has started working");

        bot.setUpdatesListener(list -> {
            list.forEach(update -> messageProcessor.checkUpdate(update, bot));

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
