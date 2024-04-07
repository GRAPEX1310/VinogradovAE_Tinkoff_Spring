package edu.java.scrapper.service.sender;

import java.net.URI;
import java.util.List;

public interface UpdateSenderService {
    void sendUpdate(Long id, URI uri, String description, List<Long> tgChatIds);
}
