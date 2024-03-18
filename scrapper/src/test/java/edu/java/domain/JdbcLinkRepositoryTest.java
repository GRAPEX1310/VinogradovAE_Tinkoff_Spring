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
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcUserRepository userRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @DisplayName("Test adding link")
    @Transactional
    @Rollback
    public void testAddingLink() {
        User user = new User(13L);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/"));
        linkRepository.addLink(user.getId(), link);
        var allLinks = linkRepository.findAllLinks();

        assertEquals(1, allLinks.size());
        assertEquals(link.getUri(), allLinks.getFirst().getUri());
    }

    @Test
    @DisplayName("Test deleting link")
    @Transactional
    @Rollback
    public void testDeletingLink() {
        User user = new User(13L);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://stackoverflow.com"));
        linkRepository.addLink(user.getId(), link);
        linkRepository.removeLinkByURL(user.getId(), link.getUri());

        var allLinks = linkRepository.findAllLinks();
        assertEquals(0, allLinks.size());
    }
}

