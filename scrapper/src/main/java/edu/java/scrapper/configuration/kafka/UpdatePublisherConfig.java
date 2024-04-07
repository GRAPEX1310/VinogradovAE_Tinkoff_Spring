package edu.java.scrapper.configuration.kafka;

import edu.java.dto.request.LinkUpdateRequest;
import edu.java.scrapper.clients.bot.BotClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.service.sender.HttpUpdateSenderService;
import edu.java.scrapper.service.sender.ScrapperQueueProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class UpdatePublisherConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public ScrapperQueueProducer scrapperQueueProducer(
        ApplicationConfig config, KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate
    ) {
        return new ScrapperQueueProducer(config, kafkaTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public HttpUpdateSenderService httpUpdateSenderService(BotClient botClient) {
        return new HttpUpdateSenderService(botClient);
    }
}
