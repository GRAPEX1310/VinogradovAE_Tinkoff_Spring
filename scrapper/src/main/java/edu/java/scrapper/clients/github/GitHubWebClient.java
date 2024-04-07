package edu.java.scrapper.clients.github;

import edu.java.scrapper.model.GitHubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GitHubWebClient implements GitHubClient {

    private final WebClient webClient;

    private final RetryTemplate retryTemplate;

    @Autowired
    public GitHubWebClient(
        WebClient.Builder webClientBuilder,
        RetryTemplate retryTemplate,
        @Value("${client.github.base-url:https://api.github.com}") String defaultURL
    ) {
        this.webClient = webClientBuilder.baseUrl(defaultURL).build();
        this.retryTemplate = retryTemplate;
    }

    @Override
    public Mono<GitHubResponse> getRepositoryData(String ownerName, String repositoryName) {
        return retryTemplate.execute(context -> webClient.get()
            .uri("/repos/{ownerName}/{repositoryName}", ownerName, repositoryName)
            .retrieve()
            .bodyToMono(GitHubResponse.class));
    }
}
