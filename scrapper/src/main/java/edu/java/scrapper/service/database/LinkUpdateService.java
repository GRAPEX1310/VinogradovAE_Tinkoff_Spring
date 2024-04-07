package edu.java.scrapper.service.database;

import edu.java.scrapper.model.Link;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface LinkUpdateService {

    Optional<String> update(Link link, OffsetDateTime updateTime);
}
