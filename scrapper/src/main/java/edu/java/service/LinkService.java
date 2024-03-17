package edu.java.service;

import edu.java.model.Link;
import java.net.URI;
import java.util.Collection;

public interface LinkService {

    Link addLink(Long chatId, URI uri);

    Link removeLinkByURL(Long chatId, URI uri);

    Collection<Link> findAllLinksForUser(Long chatId);

    Collection<Link> findLinksForUpdate(Long interval);
}
