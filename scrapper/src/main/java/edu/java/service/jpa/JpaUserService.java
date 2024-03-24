package edu.java.service.jpa;

import edu.java.domain.jpa.JpaUserRepository;
import edu.java.model.Link;
import edu.java.model.User;
import edu.java.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaUserService implements UserService {

    private final JpaUserRepository userRepository;

    @Override
    public void addUser(User user) {
        userRepository.addUser(user.getId());
    }

    @Override
    public User findUser(Long id) {
        var result = userRepository.findUser(id);
        return result.orElse(null);
    }

    @Override
    public void removeUser(Long id) {
        userRepository.removeUser(id);
    }

    @Override
    public List<Long> getUsersTrackLink(Link link) {
        return userRepository.findUsersTrackLink(link.getId());
    }
}
