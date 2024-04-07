package edu.java.scrapper.service.sender;

import edu.java.dto.request.LinkUpdateRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements UpdateSenderService {

    private final ApplicationConfig config;
    private final KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate;

    @Override
    public void sendUpdate(Long id, URI uri, String description, List<Long> tgChatIds) {
        LinkUpdateRequest request = new LinkUpdateRequest(id, uri, description, tgChatIds);
        kafkaTemplate.send(config.kafkaConfig().topics().name(), request.id(), request);
    }
}
