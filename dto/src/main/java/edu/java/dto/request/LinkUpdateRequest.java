package edu.java.dto.request;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(Long chatId, URI url, String description, List<Long> tgChatIds) {
}
