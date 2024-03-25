package edu.java.scrapper.configuration.database;

import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.JpaUserRepository;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {

    @Bean
    @Primary
    public JpaLinkRepository getJpaLinkRepository(SessionFactory sessionFactory) {
        return new JpaLinkRepository(sessionFactory);
    }

    @Bean
    @Primary
    public JpaUserRepository getJpaUserRepository(SessionFactory sessionFactory) {
        return new JpaUserRepository(sessionFactory);
    }
}
