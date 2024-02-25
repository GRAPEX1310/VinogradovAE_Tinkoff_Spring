package edu.java.bot.configuration;

import edu.java.bot.database.LinkRepository;
import edu.java.bot.database.LinkRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepositoryConfig {

    @Bean
    @Primary
    public LinkRepository createRepository() {
        return new LinkRepositoryImpl();
    }
}
