package edu.java.scrapper.configuration.database;

import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.jdbc.JdbcUserRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.UserService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.jdbc.JdbcUserService;
import edu.java.scrapper.service.updater.LinkUpdater;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {

    @Bean
    public JdbcLinkRepository getJdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public JdbcUserRepository getJdbcUserRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);
    }

    @Bean
    public LinkService getLinkService(
        JdbcLinkRepository jdbcLinkRepository,
        JdbcUserRepository jdbcUserRepository,
        List<LinkUpdater> linkUpdaters
    ) {
        return new JdbcLinkService(jdbcLinkRepository, jdbcUserRepository, linkUpdaters);
    }

    @Bean
    public UserService getUserService(JdbcUserRepository jdbcUserRepository) {
        return new JdbcUserService(jdbcUserRepository);
    }
}
