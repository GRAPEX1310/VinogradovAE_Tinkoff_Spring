package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.database.UsersLinkRepository;
import edu.java.bot.database.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements BotCommand {

    @Autowired
    private final UsersLinkRepository repository;
    private User currentUser;

    public StartCommand(UsersLinkRepository repository) {
        this.repository = repository;
    }

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
        if (repository.getUser(update.message().chat().id()) != null) {
            return new SendMessage(update.message().chat().id(), "Вы уже зарегистрированы");
        }
        boolean result = repository.addUser(update.message().chat().id());
        if (result) {
            return new SendMessage(update.message().chat().id(), this.message());
        } else {
            return new SendMessage(update.message().chat().id(), "Ошибка при регистрации");
        }
    }

    @Override
    public String message() {
        return """
            Привет! Ты только что успешно зарегистрировался в боте
            Данный бот создан для отслеживания ссылок с github и stackoverflow
            Нажми на команду /help и изучи способы взаимодействия с этим ботом
            """;
    }
}
