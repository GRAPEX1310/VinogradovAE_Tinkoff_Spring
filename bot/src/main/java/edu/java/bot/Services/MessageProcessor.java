package edu.java.bot.Services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Commands.BotCommand;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class MessageProcessor {

    private final List<BotCommand> commands;
    private final String noMessage = "No message was found";
    private final String notRequiredCommand = "Такой команды не существует";
    private final String notRequiredCommandProblem = "Incomprehensible command";
    private final String notRequiredCommandError = "This command doesnt exist";

    public String checkUpdate(Update update, TelegramBot bot) {
        if (update.message() == null) {
            log.error(noMessage);
            return noMessage;
        }

        for (BotCommand command : commands) {
            if (command.commandName().equals(update.message().text())) {
                try {
                    log.info("New message from" + command.commandName());
                    bot.execute(command.handle(update));
                } catch (Exception e) {
                    log.error("Wrong handler");
                }

                return command.message();
            }
        }

        try {
            bot.execute(new SendMessage(update.message().chat().id(), notRequiredCommand));
            log.info(notRequiredCommandProblem);
        } catch (Exception e) {
            log.error(notRequiredCommandError, e);
        }

        return notRequiredCommandProblem;
    }
}
