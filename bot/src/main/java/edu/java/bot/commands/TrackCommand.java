package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.database.LinkRepository;
import edu.java.bot.database.User;
import edu.java.bot.database.UserState;
import edu.java.bot.utils.LinkValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements BotCommand {

    private static final String RESOURCE_WAS_ADDED_RESPONSE = "Ресурс для отслеживания зарегистрирован";
    private static final String INCORRECT_RESOURCE_RESPONSE = "Некорректная ссылка, попробуйте снова";
    private static final String USER_IS_NOT_REGISTERED_RESPONSE =
        "Для начала работы нужна регистрация через команду /start";
    private static final String WAITING_LINK_RESPONSE = "Жду ссылку на ресурс, который вы хотите начать отслеживать";
    private static final String RESOURCE_HAS_ALREADY_BEEN_REGISTERED_RESPONSE = "Данный ресурс уже зарегистрирован";

    @Autowired
    private final LinkRepository repository;
    private User currentUser;

    public TrackCommand(LinkRepository repository) {
        this.repository = repository;
    }

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
        currentUser = repository.getUser(update.message().chat().id());
        if (currentUser == null) {
            return new SendMessage(update.message().chat().id(), message());
        }

        String response = "";
        if (currentUser.getState() == UserState.WAITING_TRACKING_LINK) {
            if (LinkValidator.isLinkValid(update.message().text())) {
                boolean result = repository.addLink(currentUser, update.message().text());
                if (!result) {
                    response = RESOURCE_HAS_ALREADY_BEEN_REGISTERED_RESPONSE;
                } else {
                    currentUser.setState(UserState.BASE);
                    response = RESOURCE_WAS_ADDED_RESPONSE;
                }
            } else {
                response = INCORRECT_RESOURCE_RESPONSE;
            }
            return new SendMessage(update.message().chat().id(), response);
        }

        currentUser.setState(UserState.WAITING_TRACKING_LINK);
        return new SendMessage(update.message().chat().id(), this.message());
    }

    @Override
    public String message() {
        if (currentUser == null) {
            return USER_IS_NOT_REGISTERED_RESPONSE;
        } else if (currentUser.getState() == UserState.WAITING_TRACKING_LINK) {
            return WAITING_LINK_RESPONSE;
        } else {
            return RESOURCE_WAS_ADDED_RESPONSE;
        }
    }
}
