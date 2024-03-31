package edu.java.scrapper.service.updater;

import edu.java.scrapper.model.Link;
import java.util.Optional;

public interface LinkUpdater {

    Optional<String> checkUpdate(Link link);

    boolean isAppropriateLink(Link link);
}
