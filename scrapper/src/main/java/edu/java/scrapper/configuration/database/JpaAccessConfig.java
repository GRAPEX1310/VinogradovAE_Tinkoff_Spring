package edu.java.scrapper.configuration.database;

import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.JpaUserRepository;
import edu.java.scrapper.service.database.LinkService;
import edu.java.scrapper.service.database.UserService;
import edu.java.scrapper.service.database.jpa.JpaLinkService;
import edu.java.scrapper.service.database.jpa.JpaUserService;
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
    public JpaUserRepository getJpaUserRepository(JpaLinkRepository linkRepository, SessionFactory sessionFactory) {
        return new JpaUserRepository(linkRepository, sessionFactory);
    }

    @Bean
    public LinkService getLinkService(JpaLinkRepository jpaLinkRepository) {
        return new JpaLinkService(jpaLinkRepository);
    }

    @Bean
    public UserService getUserService(JpaUserRepository jpaUserRepository) {
        return new JpaUserService(jpaUserRepository);
    }
}
