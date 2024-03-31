package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.JpaUserRepository;
import edu.java.scrapper.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository userRepository;

    @DynamicPropertySource
    public static void setJpaAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @DisplayName("Test adding user")
    @Transactional
    @Rollback
    public void testAddingUser() {
        User user = new User(26L);
        userRepository.addUser(user.getId());

        var users = userRepository.findAllUsers();
        assertEquals(user.getId(), users.getFirst().getId());
        userRepository.removeUser(26L);
    }

    @Test
    @DisplayName("Test deleting user")
    @Transactional
    @Rollback
    public void testDeletingUser() {
        User user = new User(25L);
        userRepository.addUser(user.getId());
        userRepository.removeUser(25L);

        var users = userRepository.findAllUsers();
        assertEquals(users.size(), 0);
    }

    @BeforeEach
    public void clearDB() {
        var users = userRepository.findAllUsers();
        users.forEach(user -> userRepository.removeUser(user.getId()));
    }
}
