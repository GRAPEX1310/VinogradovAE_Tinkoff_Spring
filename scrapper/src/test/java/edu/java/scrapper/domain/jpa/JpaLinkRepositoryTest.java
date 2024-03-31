package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.JpaUserRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JpaLinkRepositoryTest {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaLinkRepository linkRepository;

    @DynamicPropertySource
    public static void setJpaAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @DisplayName("Test adding link")
    @Transactional
    @Rollback
    public void testAddingLink() {
        User user = new User(26L);
        userRepository.addUser(user.getId());

        Link link = new Link(null, URI.create("https://github.com/2"));
        linkRepository.addLink(user.getId(), link);
        var allLinks = linkRepository.findAllLinks();

        assertEquals(1, allLinks.size());
        assertEquals(link.getUri(), allLinks.getFirst().getUri());
        userRepository.removeUser(26L);
    }

    @Test
    @DisplayName("Test deleting link")
    @Transactional
    @Rollback
    public void testDeletingLink() {
        User user = new User(13L);
        userRepository.addUser(user.getId());

        Link link = new Link(null, URI.create("https://stackoverflow.com/1"));
        linkRepository.addLink(user.getId(), link);
        linkRepository.removeLinkByURL(user.getId(), link.getUri());

        var allLinks = linkRepository.findAllLinks();
        assertEquals(0, allLinks.size());
    }

    @BeforeEach
    public void clearDB() {
        var users = userRepository.findAllUsers();
        users.forEach(user -> {
            var links = linkRepository.findAllUserLinks(user.getId());
            links.forEach(link -> linkRepository.removeLinkByURL(user.getId(), link.getUri()));
            userRepository.removeUser(user.getId());
        });
    }
}
