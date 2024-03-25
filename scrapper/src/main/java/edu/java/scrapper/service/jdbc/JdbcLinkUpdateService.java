package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.LinkUpdateService;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkUpdateService implements LinkUpdateService {
    private final JdbcLinkRepository linkRepository;

    @Override
    public Optional<String> update(Link link, OffsetDateTime updateTime) {
        boolean isUpdated = linkRepository.updateLink(link, updateTime);
        return Optional.ofNullable(isUpdated ? "Repository updated" : null);
    }
}
