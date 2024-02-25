package edu.java.bot;

import edu.java.bot.commands.BotCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommandsTest extends AbstractTest {

    @Test
    @DisplayName("Commands work test")
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
