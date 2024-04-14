package edu.java.bot.services;

import edu.java.bot.controllers.BotController;
import edu.java.dto.request.LinkUpdateRequest;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkUpdateService {

    private final BotController bot;
    private final Counter messageCounter;

    public void sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        linkUpdateRequest.tgChatIds().forEach(id -> {
            bot.sendUpdateMessage(id, linkUpdateRequest.description());
            log.info("Link service sent update: " + linkUpdateRequest.url() + " to user:" + id);
        });
        messageCounter.increment();
    }
}
