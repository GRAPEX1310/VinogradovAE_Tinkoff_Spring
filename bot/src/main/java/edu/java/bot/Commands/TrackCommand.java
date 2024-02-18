package edu.java.bot.Commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements BotCommand {
    @Override
    public String commandName() {
        return "/track";
    }

    @Override
    public String commandDescription() {
        return "Начать отслеживание ресурса";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), this.message());
    }

    @Override
    public String message() {
        return "Жду ссылку на ресурс, который вы хотите начать отслеживать";
    }
}
