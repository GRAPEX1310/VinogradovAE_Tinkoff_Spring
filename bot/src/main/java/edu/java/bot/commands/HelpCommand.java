package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.database.User.User;
import edu.java.bot.database.UsersLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements BotCommand {

    @Autowired
    private final UsersLinkRepository repository;

    public HelpCommand(UsersLinkRepository repository) {
        this.repository = repository;
    }

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
        User currentUser = repository.getUser(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), this.message(currentUser));
    }

    @Override
    public String message(User currentUser) {
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
