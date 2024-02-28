package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.database.UsersLinkRepository;
import edu.java.bot.database.User.User;
import edu.java.bot.database.User.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelCommand implements BotCommand {

    @Autowired
    private final UsersLinkRepository repository;
    private User currentUser;

    public CancelCommand(UsersLinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "/cancel";
    }

    @Override
    public String commandDescription() {
        return "Отменить отправку ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        currentUser = repository.getUser(update.message().chat().id());
        if (currentUser == null || currentUser.getState() == UserState.BASE) {
            return new SendMessage(update.message().chat().id(), "Нет необходимости отмены");
        }
        currentUser.setState(UserState.BASE);
        return new SendMessage(update.message().chat().id(), message());
    }

    @Override
    public String message() {
        return "Отправка ссылки отменена!";
    }
}
