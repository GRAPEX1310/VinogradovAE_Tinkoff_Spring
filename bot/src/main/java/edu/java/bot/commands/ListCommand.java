package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.database.User.User;
import edu.java.bot.database.UsersLinkRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements BotCommand {

    private static final String USER_IS_NOT_REGISTERED_RESPONSE =
        "Для начала работы нужна регистрация через команду /start";
    private static final String EMPTY_LINKS_LIST_RESPONSE = "Здесь появятся ссылки на все отслеживаемые ресурсы";

    @Autowired
    private final UsersLinkRepository repository;
    private String links;

    public ListCommand(UsersLinkRepository repository) {
        this.repository = repository;
    }

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
        User currentUser = repository.getUser(update.message().chat().id());
        if (currentUser == null) {
            return new SendMessage(update.message().chat().id(), USER_IS_NOT_REGISTERED_RESPONSE);
        }
        addLinksToList(repository.getTrackingLinks(currentUser));
        return new SendMessage(update.message().chat().id(), this.message());
    }

    @Override
    public String message() {
        if (links == null || links.isEmpty()) {
            return EMPTY_LINKS_LIST_RESPONSE;
        } else {
            return "Вот список отслеживаемых ресурсов:\n" + links;
        }
    }

    private void addLinksToList(List<String> userLinks) {
        StringBuilder resultLinksList = new StringBuilder();
        userLinks.forEach(link -> resultLinksList.append(link).append('\n'));
        links = resultLinksList.toString();
    }
}
