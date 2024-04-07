package edu.java.scrapper.configuration.database;

import edu.java.scrapper.domain.jpa.entities.LinkEntity;
import edu.java.scrapper.domain.jpa.entities.UserEntity;
import edu.java.scrapper.domain.jpa.entities.UserLinksEntity;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean(name = "entityManagerFactory")
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration()
            .addAnnotatedClass(UserEntity.class)
            .addAnnotatedClass(LinkEntity.class)
            .addAnnotatedClass(UserLinksEntity.class)
            .buildSessionFactory();
    }
}
