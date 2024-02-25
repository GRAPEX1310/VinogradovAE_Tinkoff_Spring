package edu.java.clients;

import edu.java.model.GitHubResponse;
import reactor.core.publisher.Mono;

public interface GitHubClient {
    Mono<GitHubResponse> getRepositoryData(String ownerName, String repositoryName);
}
