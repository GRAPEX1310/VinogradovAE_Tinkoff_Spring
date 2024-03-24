package edu.java.configuration.database;

import edu.java.configuration.ApplicationConfig;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.service.UserService;
import edu.java.service.jdbc.JdbcUserService;
import edu.java.service.jpa.JpaUserService;
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
