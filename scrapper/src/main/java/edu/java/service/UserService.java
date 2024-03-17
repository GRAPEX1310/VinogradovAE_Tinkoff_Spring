package edu.java.service;

import edu.java.model.Link;
import edu.java.model.User;
import java.util.List;

public interface UserService {

    void addUser(User user);

    User findUser(Long id);

    void removeUser(Long id);

    List<Long> getUsersTrackLink(Link link);
}
