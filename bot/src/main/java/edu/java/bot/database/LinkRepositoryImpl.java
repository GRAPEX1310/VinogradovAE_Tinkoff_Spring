package edu.java.bot.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepositoryImpl implements LinkRepository {

    private Map<User, Set<String>> repository;

    public LinkRepositoryImpl() {
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

    @Override
    public boolean addLink(User user, String link) {
        if (repository.get(user).contains(link)) {
            return false;
        }
        repository.computeIfAbsent(user, k -> new HashSet<>());
        repository.get(user).add(link);

        return true;
    }

    @Override
    public boolean deleteLink(User user, String link) {
        if (!repository.get(user).contains(link)) {
            return false;
        }
        repository.get(user).remove(link);
        return true;
    }

    @Override
    public List<String> getTrackingLinks(User user) {
        return new ArrayList<>(repository.get(user));
    }

    @Override
    public int getRepositorySize() {
        return repository.size();
    }
}
