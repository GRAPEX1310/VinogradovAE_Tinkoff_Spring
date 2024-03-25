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
public class UntrackCommand implements BotCommand {

    private static final String RESOURCE_WAS_DELETED_RESPONSE = "Ресурс для отслеживания удален";
    private static final String INCORRECT_RESOURCE_RESPONSE = "Некорректная ссылка, попробуйте снова";
    private static final String WAITING_LINK_RESPONSE =
        "Жду ссылку на ресурс, который вы хотите прекратить отслеживать";

    @Autowired
    private final InMemoryUserRepository repository;

    ScrapperClient scrapperClient;

    public UntrackCommand(InMemoryUserRepository repository, ScrapperClient scrapperClient) {
        this.repository = repository;
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String commandName() {
        return "/untrack";
    }

    @Override
    public String commandDescription() {
        return "Удаление ресурса из списка отслеживаемых";
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
        String response = "";
        if (currentUser.getState() == UserState.WAITING_UNTRACKING_LINK) {
            if (LinkValidator.isLinkValid(update.message().text())) {
                try {
                    scrapperClient.deleteLink(chatId, URI.create(link)).block();
                    response = RESOURCE_WAS_DELETED_RESPONSE;
                    currentUser.setState(UserState.BASE);
                } catch (ClientException e) {
                    return new SendMessage(chatId, e.getErrorResponse().exceptionMessage());
                }
            } else {
                return new SendMessage(update.message().chat().id(), INCORRECT_RESOURCE_RESPONSE);
            }

            return new SendMessage(update.message().chat().id(), response);
        }
        currentUser.setState(UserState.WAITING_UNTRACKING_LINK);
        return new SendMessage(update.message().chat().id(), this.message(currentUser));
    }

    @Override
    public String message(User currentUser) {
        return WAITING_LINK_RESPONSE;
    }

    //http://github.com
    //http://stackoverflow.com
}
