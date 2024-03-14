package edu.java.bot.clients.scrapper;

import edu.java.dto.response.LinkListResponse;
import edu.java.dto.response.LinkResponse;
import java.net.URI;
import reactor.core.publisher.Mono;

public interface ScrapperClient {
    Mono<Void> registerChat(Long chatId);

    Mono<Void> deleteChat(Long chatId);

    Mono<LinkResponse> addLink(Long chatId, URI link);

    Mono<LinkResponse> deleteLink(Long chatId, URI link);

    Mono<LinkListResponse> getLinksList(Long chatId);
}
