package edu.java.service;

import edu.java.model.Link;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface LinkUpdateService {

    Optional<String> update(Link link, OffsetDateTime updateTime);
}
