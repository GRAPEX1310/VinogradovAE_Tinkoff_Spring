package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.database.UsersLinkRepository;
import edu.java.bot.services.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractTest {

    @Autowired
    protected MessageProcessor messageProcessor;

    @MockBean
    protected TelegramBot bot;

    @MockBean
    protected UsersLinkRepository repository;
}
