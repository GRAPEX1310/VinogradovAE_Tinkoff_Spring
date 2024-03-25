package edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackages = "edu.java.client")
public class ClientConfig {
    @Bean
    public WebClient.Builder getWebClient() {
        return WebClient.builder();
    }
}
