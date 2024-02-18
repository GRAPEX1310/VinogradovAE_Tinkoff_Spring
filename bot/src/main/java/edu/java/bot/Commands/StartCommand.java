package edu.java.bot.Commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements BotCommand {

    @Override
    public String commandName() {
        return "/start";
    }

    @Override
    public String commandDescription() {
        return "Регистрация пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), this.message());
    }

    @Override
    public String message() {
        return "Регистрация выполнена";
    }
}
