package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.ScrapperRestClient;
import edu.java.bot.clients.scrapper.exeption.ClientException;
import edu.java.bot.database.User.User;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements BotCommand {
    private static final String EMPTY_LINKS_LIST_RESPONSE = "Здесь появятся ссылки на все отслеживаемые ресурсы";
    ScrapperClient scrapperClient;

    public ListCommand(ScrapperRestClient scrapperClient) {
        this.scrapperClient = scrapperClient;
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
        Long chatId = update.message().chat().id();
        StringBuilder builder = new StringBuilder();
        try {
            var response = scrapperClient.getLinksList(chatId).block();
            if (response.links().isEmpty()) {
                return new SendMessage(chatId, EMPTY_LINKS_LIST_RESPONSE);
            }
            builder.append(message(new User(0)));
            for (var link : response.links()) {
                builder.append(link.url().toString()).append("\n");
            }
        } catch (ClientException e) {
            return new SendMessage(chatId, e.getErrorResponse().exceptionMessage());
        }
        return new SendMessage(chatId, builder.toString());
    }

    @Override
    public String message(User currentUser) {
        return "Вот список отслеживаемых ресурсов:\n";
    }

}
