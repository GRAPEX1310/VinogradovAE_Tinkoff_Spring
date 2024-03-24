package edu.java.service.jpa;

import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.model.Link;
import edu.java.service.LinkUpdateService;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkUpdateService implements LinkUpdateService {

    private final JpaLinkRepository linkRepository;

    @Override
    public Optional<String> update(Link link, OffsetDateTime updateTime) {
        boolean isUpdated = linkRepository.updateLink(link, updateTime);
        return Optional.ofNullable(isUpdated ? "Repository updated" : null);
    }
}
