package sk.upjs.ics.spring.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sk.upjs.ics.spring.akka.SpringExtension;

import javax.sql.DataSource;


/**
 * Spring configuration for production enviroment
 *
 * @author Tomas Ondrej
 */
@Configuration
@ComponentScan({"sk.upjs.ics.actors", "sk.upjs.ics.services"})
@EnableTransactionManagement
public class ProdConfig extends Config {

    @Override
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("prod");
        return factoryBean;
    }

    @Bean
    public DataSource getDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("sql/insert_test_data.sql")
                .build();
    }

    @Bean
    @Override
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("ajs-actor-system");
        SpringExtension.SPRING_EXTENSION_PROVIDER
                .get(system)
                .initialize(applicationContext);
        return system;
    }

    @Bean
    public ActorRef entitySupervisorRef() throws Exception {
        return actorSystem()
                .actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER
                        .get(actorSystem())
                        .create("entitySupervisor"), "supervisor");
    }
}