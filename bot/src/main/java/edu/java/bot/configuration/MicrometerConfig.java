package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    @Bean
    public Counter messagesCounter(MeterRegistry registry, ApplicationConfig config) {
        return Counter.builder(config.micrometerConfig().processedMessagesCounter().name())
            .description(config.micrometerConfig().processedMessagesCounter().description())
            .register(registry);
    }
}
