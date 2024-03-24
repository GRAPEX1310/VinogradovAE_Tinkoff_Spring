package edu.java.service.jpa;

import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.model.Link;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;

    @Override
    public Link addLink(Long chatId, URI uri) {
        Link link = new Link(null, uri);
        linkRepository.addLink(chatId, link);
        return link;
    }

    @Override
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
}
