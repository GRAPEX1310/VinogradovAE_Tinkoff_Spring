package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.exeption.ClientException;
import edu.java.bot.database.InMemoryUserRepository;
import edu.java.bot.database.User.User;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements BotCommand {

    ScrapperClient scrapperClient;

    public StartCommand(InMemoryUserRepository repository, ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
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
        Long chatId = update.message().chat().id();
        try {
            scrapperClient.registerChat(chatId).block();
        } catch (ClientException e) {
            return new SendMessage(chatId, e.getErrorResponse().exceptionMessage());
        }
        return new SendMessage(chatId, message(new User(0)));
    }

    @Override
    public String message(User currentUser) {
        return """
            Привет! Ты только что успешно зарегистрировался в боте
            Данный бот создан для отслеживания ссылок с github и stackoverflow
            Нажми на команду /help и изучи способы взаимодействия с этим ботом
            """;
    }
}
