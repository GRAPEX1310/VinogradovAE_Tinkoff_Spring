package edu.java.bot.database;

import edu.java.bot.database.User.User;

public interface InMemoryUserRepository {

    boolean addUser(long chatId);

    User getUser(long chatId);
}
