package edu.java.domain;

import edu.java.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void addUser(Long userId);

    void removeUser(Long userId);

    Optional<User> findUser(Long userId);

    List<Long> findUsersTrackLink(Long linkId);
}
