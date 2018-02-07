package sk.upjs.ics;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sk.upjs.ics.spring.config.ProdConfig;

/**
 * @author Tomas Ondrej
 */
public class Runner {

    public static void main(String[] args) {
        Runner.start();
    }

    private static void start() {
        new AnnotationConfigApplicationContext(ProdConfig.class);
    }
}
