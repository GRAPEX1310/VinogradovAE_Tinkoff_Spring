package edu.java.dto.request;

import java.util.List;

public record LinkUpdateRequest(Long chatId, String url, String description, List<Long> tgChatIds) {
}
