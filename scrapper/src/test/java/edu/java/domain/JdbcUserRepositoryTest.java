package edu.java.domain;

import edu.java.model.Link;
import edu.java.model.User;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcUserRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcUserRepository userRepository;

    @Test
    @DisplayName("Test adding user")
    @Transactional
    @Rollback
    public void testAddingLink() {
        User user = new User(13L);
        userRepository.addUser(user);

        var users = userRepository.findAllUsers();
        assertEquals(user, users.getFirst());
    }

    @Test
    @DisplayName("Test deleting user")
    @Transactional
    @Rollback
    public void testDeletingLink() {
        User user = new User(13L);
        userRepository.addUser(user);
        userRepository.removeUser(13L);

        var users = userRepository.findAllUsers();
        assertEquals(users.size(), 0);
    }
}
