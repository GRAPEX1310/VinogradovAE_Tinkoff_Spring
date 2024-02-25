package edu.java.clients;

import edu.java.model.StackOverflowResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StackOverflowWebClient implements StackOverflowClient {

    private final WebClient webClient;

    @Autowired
    public StackOverflowWebClient(
        WebClient.Builder webClientBuilder,
        @Value ("${client.stackoverflow.base-url:https://api.stackexchange.com/2.3}") String defaultURL
    ) {
        this.webClient = webClientBuilder.baseUrl(defaultURL).build();
    }

    @Override
    public Mono<Optional<StackOverflowResponse>> getQuestionData(int questionId) {
        return webClient.get()
            .uri("/questions/{questionId}?site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowResponse.StackOverflowItemResponseList.class)
            .map(resp -> Optional.ofNullable(!resp.itemsList().isEmpty() ? resp.itemsList().getFirst() : null));
    }
}
