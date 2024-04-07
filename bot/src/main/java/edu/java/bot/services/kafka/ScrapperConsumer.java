package edu.java.bot.services.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.services.LinkUpdateService;
import edu.java.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperConsumer {
    private final LinkUpdateService linkUpdateService;
    private final ApplicationConfig config;
    private final KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate;

    @KafkaListener(groupId = "scrapper.updates.listeners",
                   topics = "${app.kafka-config.topics.name}",
                   containerFactory = "linkUpdateRequestConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload LinkUpdateRequest request) {
        try {
            linkUpdateService.sendUpdates(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            kafkaTemplate.send(config.kafkaConfig().topics().name() + "_dlq", request.id(), request);
        }
    }

}
