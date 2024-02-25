package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.BotCommand;
import edu.java.bot.database.LinkRepository;
import edu.java.bot.database.User;
import edu.java.bot.database.UserState;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class MessageProcessor {

    private static final String NOT_REQUIRED_COMMAND_RESPONSE = "Такой команды не существует";
    private static final String NOT_REQUIRED_COMMAND_PROBLEM_LOG = "Incomprehensible command";
    private static final String NOT_REQUIRED_COMMAND_ERROR_LOG = "This command doesnt exist";
    private static final String NO_MESSAGE_WAS_FOUND_LOG = "No message was found";
    private static final String MESSAGE_FROM_COMMAND_LOG = "New message from - ";
    private static final String WRONG_HANDLER_ERROR_LOG = "Wrong handler";

    @Autowired
    private LinkRepository repository;
    private final List<BotCommand> commands;

    public String checkUpdate(Update update, TelegramBot bot) {
        if (update.message() == null) {
            log.error(NO_MESSAGE_WAS_FOUND_LOG);
            return NO_MESSAGE_WAS_FOUND_LOG;
        }

        User currentUser = repository.getUser(update.message().chat().id());

        for (BotCommand command : commands) {
            if (currentUser != null || command.commandName().equals(update.message().text())) {
                if (currentUser != null) {
                    if (currentUser.getState() == UserState.WAITING_TRACKING_LINK
                        && command.commandName().equals("/track")) {
                        try {
                            log.info(MESSAGE_FROM_COMMAND_LOG + command.commandName());
                            bot.execute(command.handle(update));
                        } catch (Exception e) {
                            log.error(WRONG_HANDLER_ERROR_LOG);
                        }
                    } else if (currentUser.getState() == UserState.WAITING_UNTRACKING_LINK
                        && command.commandName().equals("/untrack")) {
                        try {
                            log.info(MESSAGE_FROM_COMMAND_LOG + command.commandName());
                            bot.execute(command.handle(update));
                        } catch (Exception e) {
                            log.error(WRONG_HANDLER_ERROR_LOG);
                        }
                    }
                } else {
                    try {
                        log.info(MESSAGE_FROM_COMMAND_LOG + command.commandName());
                        bot.execute(command.handle(update));
                    } catch (Exception e) {
                        log.error(WRONG_HANDLER_ERROR_LOG);
                    }
                }

                return command.message();
            }
        }

        try {
            bot.execute(new SendMessage(update.message().chat().id(), NOT_REQUIRED_COMMAND_RESPONSE));
            log.info(NOT_REQUIRED_COMMAND_PROBLEM_LOG);
        } catch (Exception e) {
            log.error(NOT_REQUIRED_COMMAND_ERROR_LOG, e);
        }

        return NOT_REQUIRED_COMMAND_PROBLEM_LOG;
    }
}
