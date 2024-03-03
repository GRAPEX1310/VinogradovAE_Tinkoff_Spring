package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements BotCommand {
    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public String commandDescription() {
        return "Помощь во взаимодействии с ботом";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), this.message());
    }

    @Override
    public String message() {
        return """
            Как взаимодействовать с ботом?
            Слева снизу находится кнопка "Меню", в которой показаны и описаны все команды данного бота
            /start - регистрация пользователя
            /help - помощь во взаимодействии с ботом
            /track - начать отслеживание обновлений ресурса
            /untrack - прекратить отслеживание обновлений ресурса
            /list - список отслеживаемых ресурсов
            /cancel - отменить ввод ссылки
            """;
    }
}
