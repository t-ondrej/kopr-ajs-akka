package sk.upjs.ics.spring.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sk.upjs.ics.spring.akka.SpringExtension;

import javax.sql.DataSource;

/**
 * Spring configuration for testing enviroment
 *
 * @author Tomas Ondrej
 */
@Configuration
@ComponentScan({"sk.upjs.ics.actors", "sk.upjs.ics.services"})
@EnableTransactionManagement
public class TestConfig extends Config {

    @Override
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("test");
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
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("ajs-actor-system");
        SpringExtension.SPRING_EXTENSION_PROVIDER
                .get(system)
                .initialize(applicationContext);
        return system;
    }

    @Bean
    public ActorRef attendeeActorRef() throws Exception {
        return actorSystem()
                .actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER
                            .get(actorSystem())
                            .create("attendeeActor")
                            .withRouter(new RoundRobinPool(3)),
                        "attendeeActor");
    }

    @Bean
    public ActorRef lectureActorRef() throws Exception {
        return actorSystem()
                .actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER
                            .get(actorSystem())
                            .create("lectureActor")
                            .withRouter(new RoundRobinPool(3)),
                        "lectureActor");
    }

    @Bean
    public ActorRef attendanceSheetActorRef() throws Exception {
        return actorSystem()
                .actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER
                            .get(actorSystem())
                            .create("attendanceSheetActor")
                            .withRouter(new RoundRobinPool(3)),
                        "attendanceSheetActor");
    }
}