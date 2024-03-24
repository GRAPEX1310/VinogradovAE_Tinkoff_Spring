package edu.java.domain;

import edu.java.model.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {

    void addLink(Long userId, Link link);

    Link removeLinkByURL(Long userId, URI uri);

    List<Link> findAllUserLinks(Long userId);

    List<Link> findAllLinksWithCheckInterval(Long interval);

    boolean updateLink(Link link, OffsetDateTime updateTime);
}
