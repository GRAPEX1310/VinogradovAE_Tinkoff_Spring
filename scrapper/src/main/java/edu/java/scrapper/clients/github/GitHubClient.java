package edu.java.scrapper.clients.github;

import edu.java.scrapper.model.GitHubResponse;
import reactor.core.publisher.Mono;

public interface GitHubClient {
    Mono<GitHubResponse> getRepositoryData(String ownerName, String repositoryName);
}
