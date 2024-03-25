package edu.java.scrapper.service;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import java.util.List;

public interface UserService {

    void addUser(User user);

    User findUser(Long id);

    void removeUser(Long id);

    List<Long> getUsersTrackLink(Link link);
}
