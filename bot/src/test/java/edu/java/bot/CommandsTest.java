package edu.java.bot;

import edu.java.bot.Commands.BotCommand;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommandsTest extends BaseForTest {

    @Test
    public void testCommands() {
        List<? extends BotCommand> commands = messageProcessor.getCommands();

        assertThat(commands.stream().map(BotCommand::commandName).toArray()).contains("/help", "/list", "/start", "/track", "/untrack");
    }
}
