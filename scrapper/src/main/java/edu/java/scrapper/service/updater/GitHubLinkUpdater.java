package edu.java.scrapper.service.updater;

import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.LinkUpdateService;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class GitHubLinkUpdater implements LinkUpdater {
    private static final String HOST = "github.com";
    private final GitHubClient gitHubClient;
    private final LinkUpdateService linkUpdateService;

    @Autowired
    public GitHubLinkUpdater(GitHubClient gitHubClient, LinkUpdateService linkUpdateService) {
        this.gitHubClient = gitHubClient;
        this.linkUpdateService = linkUpdateService;
    }

    @Override
    public Optional<String> checkUpdate(Link link) {
        String[] segments = link.getUri().getPath().split("/");
        String owner = segments[1];
        String repository = segments[2];
        var response = gitHubClient.getRepositoryData(owner, repository);
        return linkUpdateService.update(link, Objects.requireNonNull(response.block()).updatedAt());
    }

    @Override
    public boolean isAppropriateLink(Link link) {
        return link.getUri().getHost().equals(HOST);
    }
}
