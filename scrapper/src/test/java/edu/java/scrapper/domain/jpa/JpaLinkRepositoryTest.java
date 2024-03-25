package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.JpaUserRepository;
import edu.java.scrapper.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JpaLinkRepositoryTest {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Test
    @DisplayName("Test adding user")
    @Transactional
    @Rollback
    public void testAddingLink() {
        User user = new User(26L);
        userRepository.addUser(user.getId());

        var users = userRepository.findAllUsers();
        assertEquals(user.getId(), users.getFirst().getId());
    }

    @Test
    @DisplayName("Test deleting user")
    @Transactional
    @Rollback
    public void testDeletingLink() {
        User user = new User(13L);
        userRepository.addUser(user.getId());
        userRepository.removeUser(13L);

        var users = userRepository.findAllUsers();
        assertEquals(users.size(), 0);
    }

    @BeforeEach
    public void clearDB() {
        var users = userRepository.findAllUsers();
        users.forEach(user -> userRepository.removeUser(user.getId()));
    }
}
