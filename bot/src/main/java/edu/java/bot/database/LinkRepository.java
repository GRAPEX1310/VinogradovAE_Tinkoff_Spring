package edu.java.bot.database;

import java.util.List;

public interface LinkRepository {

    boolean addLink(User user, String link);

    boolean deleteLink(User user, String link);

    List<String> getTrackingLinks(User user);

    boolean addUser(long chatId);

    User getUser(long chatId);

    int getRepositorySize();
}
