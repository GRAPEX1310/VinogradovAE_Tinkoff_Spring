package edu.java.configuration.database;

import edu.java.configuration.ApplicationConfig;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.service.LinkService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.updater.LinkUpdater;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkServiceConfig {

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private JdbcUserRepository jdbcUserRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Bean
    public LinkService getLinkService(ApplicationConfig applicationConfig, List<LinkUpdater> linkUpdaters) {
        switch (applicationConfig.databaseAccessType()) {
            case JDBC -> {
                return new JdbcLinkService(jdbcLinkRepository, jdbcUserRepository, linkUpdaters);
            }
            case JPA -> {
                return new JpaLinkService(jpaLinkRepository);
            }
            default -> {
                return null;
            }
        }
    }
}