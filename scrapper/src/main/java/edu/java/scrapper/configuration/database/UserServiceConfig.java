package edu.java.scrapper.configuration.database;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.jdbc.JdbcUserRepository;
import edu.java.scrapper.domain.jpa.JpaUserRepository;
import edu.java.scrapper.service.UserService;
import edu.java.scrapper.service.jdbc.JdbcUserService;
import edu.java.scrapper.service.jpa.JpaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfig {

    @Autowired
    private JdbcUserRepository jdbcUserRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Bean
    public UserService getUserService(ApplicationConfig applicationConfig) {
        switch (applicationConfig.databaseAccessType()) {
            case JDBC -> {
                return new JdbcUserService(jdbcUserRepository);
            }
            case JPA -> {
                return new JpaUserService(jpaUserRepository);
            }
            default -> {
                return null;
            }
        }
    }
}
