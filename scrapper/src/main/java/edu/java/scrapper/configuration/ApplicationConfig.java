package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    AccessType databaseAccessType,
    @Bean
    @NotNull
    Scheduler scheduler,
    Retry retry

    ) {
    public record Scheduler(
        boolean enable,
        @NotNull Duration interval,
        @NotNull Duration forceCheckDelay,
        @NotNull Duration checkInterval
    ) {
    }

    public record Retry(
        Integer maxAttempts,
        Set<Integer> retryStatusCodes,
        RetryType type,
        DelayConfig delayConfig
    ) {
        public enum RetryType {
            CONSTANT, LINEAR, EXPONENTIAL
        }

        public record DelayConfig(
            ConstantConfig constant,
            LinearConfig linear,
            ExponentialConfig exponential
        ) {
            public record ConstantConfig(
                Long backOffPeriodMillis
            ) {
            }

            public record LinearConfig(
                Long initialIntervalMillis,
                Long maxIntervalMillis
            ) {
            }

            public record ExponentialConfig(
                Long initialIntervalMillis,
                Double multiplier,
                Long maxIntervalMillis
            ) {
            }
        }
    }
}
