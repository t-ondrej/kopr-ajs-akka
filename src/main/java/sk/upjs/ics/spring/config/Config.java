package sk.upjs.ics.spring.config;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Base spring configuration
 *
 * @author Tomas Ondrej
 */
public abstract class Config {

    @Autowired
    protected ApplicationContext applicationContext;

    @Bean
    public abstract DataSource getDataSource();

    @Bean
    public abstract LocalEntityManagerFactoryBean entityManagerFactory();

    @Bean
    public abstract ActorSystem actorSystem();

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
