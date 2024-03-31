package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.controller.exception.UserNotFoundException;
import edu.java.scrapper.domain.JdbcUserRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcUserService implements UserService {

    private final JdbcUserRepository userRepository;

    @Autowired
    public JdbcUserService(JdbcUserRepository jdbcUserRepository) {
        userRepository = jdbcUserRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
    }

    @Override
    public User findUser(Long id) {
        Optional<User> user = userRepository.findUser(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return user.get();
    }

    @Override
    public void removeUser(Long id) {
        userRepository.removeUser(id);
    }

    @Override
    public List<Long> getUsersTrackLink(Link link) {
        return userRepository.findUsersTrackLink(link);
    }
}
