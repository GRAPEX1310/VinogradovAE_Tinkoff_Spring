package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.model.StackOverflowResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StackOverflowWebClient implements StackOverflowClient {

    private final WebClient webClient;

    private final RetryTemplate retryTemplate;

    @Autowired
    public StackOverflowWebClient(
        WebClient.Builder webClientBuilder,
        RetryTemplate retryTemplate,
        @Value("${client.stackoverflow.base-url:https://api.stackexchange.com/2.3}") String defaultURL
    ) {
        this.webClient = webClientBuilder.baseUrl(defaultURL).build();
        this.retryTemplate = retryTemplate;
    }

    @Override
    public Mono<Optional<StackOverflowResponse>> getQuestionData(int questionId) {
        return retryTemplate.execute(context -> webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{questionId}")
                .queryParam("site", "stackoverflow")
                .build(questionId)
            )
            .retrieve()
            .bodyToMono(StackOverflowResponse.StackOverflowItemResponseList.class)
            .map(resp -> Optional.ofNullable(!resp.itemsList().isEmpty() ? resp.itemsList().getFirst() : null)));
    }
}
