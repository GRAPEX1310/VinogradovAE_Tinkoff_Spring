package edu.java.bot.Commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements BotCommand {
    @Override
    public String commandName() {
        return "/list";
    }

    @Override
    public String commandDescription() {
        return "Список отслеживаемых ресурсов";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), this.message());
    }

    @Override
    public String message() {
        return "Здесь будет список отслеживаемых ресурсов. А пока что - заглушка 👌";
    }
}
