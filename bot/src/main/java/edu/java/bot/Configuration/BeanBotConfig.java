package edu.java.bot.Configuration;

import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BeanBotConfig {

    @Bean
    public TelegramBot getBot(ApplicationConfig config) {
        if (config.telegramToken() == null) {
            log.error("Token was not found");
        }

        return new TelegramBot(config.telegramToken());
    }
}
