package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.BotCommand;
import edu.java.bot.database.User.User;
import edu.java.bot.database.User.UserState;
import edu.java.bot.database.UsersLinkRepository;
import java.util.List;
import java.util.Map;
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
    private UsersLinkRepository repository;
    private final List<BotCommand> commands;
    private final Map<String, BotCommand> commandMap;

    public void start() {
        commandMap.clear();
        for (BotCommand command : commands) {
            commandMap.put(command.commandName(), command);
        }
    }

    public String checkUpdate(Update update, TelegramBot bot) {
        if (update.message() == null) {
            log.error(NO_MESSAGE_WAS_FOUND_LOG);
            return NO_MESSAGE_WAS_FOUND_LOG;
        }

        User currentUser = repository.getUser(update.message().chat().id());
        BotCommand command = null;
        if (commandMap.get(update.message().text()) != null) {
            command = commandMap.get(update.message().text());
        } else if (currentUser != null) {
            if (currentUser.getState() == UserState.WAITING_TRACKING_LINK) {
                command = commandMap.get("/track");
            } else if (currentUser.getState() == UserState.WAITING_UNTRACKING_LINK) {
                command = commandMap.get("/untrack");
            }
        }
        if (command != null) {
            try {
                log.info(MESSAGE_FROM_COMMAND_LOG + command.commandName());
                bot.execute(command.handle(update));
            } catch (Exception e) {
                log.error(WRONG_HANDLER_ERROR_LOG);
            }
            return command.message(currentUser);
        } else {
            try {
                bot.execute(new SendMessage(update.message().chat().id(), NOT_REQUIRED_COMMAND_RESPONSE));
                log.info(NOT_REQUIRED_COMMAND_PROBLEM_LOG);
            } catch (Exception e) {
                log.error(NOT_REQUIRED_COMMAND_ERROR_LOG, e);
            }
            return NOT_REQUIRED_COMMAND_PROBLEM_LOG;
        }
    }
}
