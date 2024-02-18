package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.Services.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {BotApplication.class})
public class BaseForTest {

    @Autowired
    protected MessageProcessor messageProcessor;

    @MockBean
    protected TelegramBot bot;
}
