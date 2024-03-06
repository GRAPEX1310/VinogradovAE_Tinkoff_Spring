package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.BotCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.database.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class CommandsTest extends AbstractTest {

    @Test
    @DisplayName("Commands list work test")
    public void testCommands() {
        List<? extends BotCommand> commands = messageProcessor.getCommands();

        assertThat(commands.stream().map(BotCommand::commandName).toArray()).contains(
            "/help",
            "/list",
            "/start",
            "/track",
            "/untrack",
            "/cancel"
        );
    }
}
