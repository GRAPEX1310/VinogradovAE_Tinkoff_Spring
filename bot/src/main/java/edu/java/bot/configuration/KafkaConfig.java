package edu.java.bot.configuration;

import edu.java.dto.request.LinkUpdateRequest;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic updatesTopic(ApplicationConfig config) {
        return TopicBuilder
            .name(config.kafkaConfig().topics().name())
            .partitions(config.kafkaConfig().topics().partitions())
            .replicas(config.kafkaConfig().topics().replicas())
            .build();
    }

    @Bean
    public NewTopic updateDlqTopic(ApplicationConfig config) {
        return TopicBuilder
            .name(config.kafkaConfig().topics().name() + "_dlq")
            .partitions(config.kafkaConfig().topics().partitions())
            .replicas(config.kafkaConfig().topics().replicas())
            .build();
    }

    @Bean
    public ConsumerFactory<Long, LinkUpdateRequest> linkUpdateRequestConsumerFactory(ApplicationConfig config) {
        return new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaConfig().servers(),
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonDeserializer.TRUSTED_PACKAGES, "*",
            JsonDeserializer.USE_TYPE_INFO_HEADERS, false,
            JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest>
    linkUpdateRequestConcurrentKafkaListenerContainerFactory(ConsumerFactory<Long, LinkUpdateRequest> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest> kafkaListenerContainerFactory =
            new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        return kafkaListenerContainerFactory;
    }

    @Bean
    public ProducerFactory<Long, LinkUpdateRequest> linkUpdateRequestProducerFactory(ApplicationConfig config) {
        return new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaConfig().servers(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongDeserializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonSerializer.ADD_TYPE_INFO_HEADERS, false
        ));
    }

    @Bean
    public KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate(ProducerFactory<Long, LinkUpdateRequest> factory) {
        return new KafkaTemplate<>(factory);
    }
}
