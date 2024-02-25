package edu.java.clients;

import edu.java.model.GitHubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GitHubWebClient implements GitHubClient {

    private final WebClient webClient;

    @Autowired
    public GitHubWebClient(
        WebClient.Builder webClientBuilder,
        @Value ("${client.github.base-url:https://api.github.com}") String defaultURL
    ) {
        this.webClient = webClientBuilder.baseUrl(defaultURL).build();
    }

    @Override
    public Mono<GitHubResponse> getRepositoryData(String ownerName, String repositoryName) {
        return webClient.get()
            .uri("/repos/{ownerName}/{repositoryName}", ownerName, repositoryName)
            .retrieve()
            .bodyToMono(GitHubResponse.class);
    }
}
