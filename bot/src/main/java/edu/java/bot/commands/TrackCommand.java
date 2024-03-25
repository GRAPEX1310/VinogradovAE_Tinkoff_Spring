package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.exeption.ClientException;
import edu.java.bot.database.InMemoryUserRepository;
import edu.java.bot.database.User.User;
import edu.java.bot.database.User.UserState;
import edu.java.bot.utils.LinkValidator;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements BotCommand {

    private static final String RESOURCE_WAS_ADDED_RESPONSE = "Ресурс для отслеживания зарегистрирован";
    private static final String INCORRECT_RESOURCE_RESPONSE = "Некорректная ссылка, попробуйте снова";
    private static final String WAITING_LINK_RESPONSE = "Жду ссылку на ресурс, который вы хотите начать отслеживать";

    @Autowired
    private final InMemoryUserRepository repository;

    ScrapperClient scrapperClient;

    public TrackCommand(InMemoryUserRepository repository, ScrapperClient scrapperClient) {
        this.repository = repository;
        this.scrapperClient = scrapperClient;
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
        User currentUser = repository.getUser(update.message().chat().id());
        Long chatId = update.message().chat().id();
        String link = update.message().text();

        if (currentUser == null) {
            repository.addUser(chatId);
        }
        currentUser = repository.getUser(chatId);
        String response;
        if (currentUser.getState() == UserState.WAITING_TRACKING_LINK) {
            if (LinkValidator.isLinkValid(link)) {
                try {
                    scrapperClient.addLink(chatId, URI.create(link)).block();
                    response = RESOURCE_WAS_ADDED_RESPONSE;
                    currentUser.setState(UserState.BASE);
                } catch (ClientException e) {
                    return new SendMessage(chatId, e.getErrorResponse().exceptionMessage());
                }
            } else {
                response = INCORRECT_RESOURCE_RESPONSE;
            }
            return new SendMessage(chatId, response);
        }

        currentUser.setState(UserState.WAITING_TRACKING_LINK);
        return new SendMessage(chatId, message(currentUser));
    }

    @Override
    public String message(User currentUser) {
        return WAITING_LINK_RESPONSE;
    }
}
