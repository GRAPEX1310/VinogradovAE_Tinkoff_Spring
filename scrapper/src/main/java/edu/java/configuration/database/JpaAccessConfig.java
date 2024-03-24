package edu.java.configuration.database;

import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaUserRepository;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {

    @Bean
    public JpaLinkRepository getJpaLinkRepository(SessionFactory sessionFactory) {
        return new JpaLinkRepository(sessionFactory);
    }

    @Bean
    public JpaUserRepository getJpaUserRepository(SessionFactory sessionFactory) {
        return new JpaUserRepository(sessionFactory);
    }
}
