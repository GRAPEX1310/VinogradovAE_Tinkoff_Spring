package edu.java.scrapper.service.sender;

import edu.java.scrapper.clients.bot.BotClient;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpUpdateSenderService implements UpdateSenderService {

    private final BotClient botClient;

    @Override
    public void sendUpdate(Long id, URI uri, String description, List<Long> tgChatIds) {
        botClient.sendUpdates(id, uri, description, tgChatIds).block();
    }
}
