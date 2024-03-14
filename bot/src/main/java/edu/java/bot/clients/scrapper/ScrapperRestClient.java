package edu.java.bot.clients.scrapper;

import edu.java.bot.clients.scrapper.exeption.ClientException;
import edu.java.dto.request.DeleteLinkRequest;
import edu.java.dto.request.SetLinkRequest;
import edu.java.dto.response.ClientErrorResponse;
import edu.java.dto.response.LinkListResponse;
import edu.java.dto.response.LinkResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ScrapperRestClient implements ScrapperClient {

    private static final String CHAT_PREFIX = "/tg-chat/";
    private static final String CHAT_HEADER = "Tg-Chat-Id";
    private static final String LINKS_ENDPOINT = "/links";

    private final WebClient webClient;

    @Autowired
    public ScrapperRestClient(
        WebClient.Builder webClientBuilder,
        @Value("${client.scrapper.base-url:http://localhost:8080}") String defaultURL
    ) {
        webClient = webClientBuilder.baseUrl(defaultURL).build();
    }

    @Override
    public Mono<Void> registerChat(Long chatId) {
        return webClient.post()
            .uri(CHAT_PREFIX + chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ClientErrorResponse.class)
                    .flatMap(errorResponse ->
                        Mono.error(new ClientException(errorResponse))))
            .bodyToMono(Void.class);
    }

    @Override
    public Mono<Void> deleteChat(Long chatId) {
        return webClient.delete()
            .uri(CHAT_PREFIX + chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ClientErrorResponse.class)
                    .flatMap(errorResponse ->
                        Mono.error(new ClientException(errorResponse))))
            .bodyToMono(Void.class);
    }

    @Override
    public Mono<LinkResponse> addLink(Long chatId, URI link) {
        return webClient.post()
            .uri(LINKS_ENDPOINT)
            .header(CHAT_HEADER, chatId.toString())
            .bodyValue(new SetLinkRequest(link.toString()))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ClientErrorResponse.class)
                    .flatMap(errorResponse ->
                        Mono.error(new ClientException(errorResponse))))
            .bodyToMono(LinkResponse.class);
    }

    @Override
    public Mono<LinkResponse> deleteLink(Long chatId, URI link) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(CHAT_HEADER, chatId.toString())
            .body(Mono.just(new DeleteLinkRequest(link.toString())), DeleteLinkRequest.class)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ClientErrorResponse.class)
                    .flatMap(errorResponse ->
                        Mono.error(new ClientException(errorResponse))))
            .bodyToMono(LinkResponse.class);
    }

    @Override
    public Mono<LinkListResponse> getLinksList(Long chatId) {
        return webClient.get()
            .uri(LINKS_ENDPOINT)
            .header(CHAT_HEADER, chatId.toString())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ClientErrorResponse.class)
                    .flatMap(errorResponse ->
                        Mono.error(new ClientException(errorResponse))))
            .bodyToMono(LinkListResponse.class);

    }
}
