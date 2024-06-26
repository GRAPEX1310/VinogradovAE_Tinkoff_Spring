package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.controller.exception.UserNotFoundException;
import edu.java.scrapper.controller.exception.WrongRequestParametersException;
import edu.java.scrapper.domain.JdbcLinkRepository;
import edu.java.scrapper.domain.JdbcUserRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.updater.LinkUpdater;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;
    private final JdbcUserRepository userRepository;
    private final List<LinkUpdater> linkUpdaters;

    @Autowired
    public JdbcLinkService(
        JdbcLinkRepository jdbcLinkRepository,
        JdbcUserRepository jdbcUserRepository,
        List<LinkUpdater> linkUpdaterList
    ) {
        linkRepository = jdbcLinkRepository;
        userRepository = jdbcUserRepository;
        linkUpdaters = linkUpdaterList;
    }

    @Override
    @Transactional
    public Link addLink(Long chatId, URI uri) {
        Link link = new Link(null, uri);

        for (LinkUpdater linkUpdater : linkUpdaters) {
            if (linkUpdater.isAppropriateLink(link)) {
                try {
                    linkUpdater.checkUpdate(link);
                } catch (RuntimeException e) {
                    throw new WrongRequestParametersException("Wrong link!");
                }
            }
        }

        linkRepository.addLink(chatId, link);
        return link;
    }

    @Override
    @Transactional
    public Link removeLinkByURL(Long chatId, URI uri) {
        return linkRepository.removeLinkByURL(chatId, uri);
    }

    @Override
    public Collection<Link> findAllLinksForUser(Long chatId) {
        return linkRepository.findAllUserLinks(chatId);
    }

    @Override
    public Collection<Link> findLinksForUpdate(Long interval) {
        return linkRepository.findAllLinksWithCheckInterval(interval);
    }

    private User findUser(Long userId) {
        Optional<User> user = userRepository.findUser(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        return user.get();
    }
}
