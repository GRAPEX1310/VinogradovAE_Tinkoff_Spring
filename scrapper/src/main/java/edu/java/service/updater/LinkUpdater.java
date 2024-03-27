package edu.java.service.updater;

import edu.java.model.Link;
import java.util.Optional;

public interface LinkUpdater {

    Optional<String> checkUpdate(Link link);

    boolean isAppropriateLink(Link link);
}
