package edu.java.bot.database;

import edu.java.bot.database.User.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepositoryImpl implements InMemoryUserRepository {

    private Map<User, Set<String>> repository;

    public InMemoryUserRepositoryImpl() {
        this.repository = new HashMap<>();
    }

    @Override
    public boolean addUser(long chatId) {
        User user = new User(chatId);
        if (repository.containsKey(user)) {
            return false;
        }
        repository.put(user, new HashSet<>());
        return true;
    }

    @Override
    public User getUser(long chatId) {
        for (User user : repository.keySet()) {
            if (user.getId() == chatId) {
                return user;
            }
        }
        return null;
    }
}
