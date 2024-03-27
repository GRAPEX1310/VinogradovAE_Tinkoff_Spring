package edu.java.clients.bot;

import edu.java.clients.exception.ClientException;
import edu.java.dto.request.LinkUpdateRequest;
import edu.java.dto.response.ClientErrorResponse;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BotRestClient implements BotClient {

    private static final String ENDPOINT = "/updates";
    private final WebClient webClient;

    @Autowired
    public BotRestClient(
        WebClient.Builder webClientBuilder,
        @Value("${client.bot.base-url:http://localhost:8090}") String defaultURL
    ) {
        this.webClient = webClientBuilder.baseUrl(defaultURL).build();
    }

    @Override
    public Mono<Void> sendUpdates(Long id, URI uri, String description, List<Long> tgChatIds) {
        return webClient
            .post()
            .uri(ENDPOINT)
            .bodyValue(new LinkUpdateRequest(id, uri, description, tgChatIds))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new ClientException(clientErrorResponse))))
            .bodyToMono(Void.class);
    }
}
