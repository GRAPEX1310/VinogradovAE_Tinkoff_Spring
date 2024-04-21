package edu.java.scrapper.domain;

import edu.java.scrapper.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void addUser(Long userId);

    void removeUser(Long userId);

    Optional<User> findUser(Long userId);

    List<Long> findUsersTrackLink(Long linkId);

    List<User> findAllUsers();
}
