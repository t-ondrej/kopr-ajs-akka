package sk.upjs.ics.spring.akka;

import akka.actor.*;
import org.springframework.context.ApplicationContext;

public class SpringExtension extends AbstractExtensionId<SpringExtension.SpringExt> {
 
    public static final SpringExtension SPRING_EXTENSION_PROVIDER = new SpringExtension();
 
    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }
 
    public static class SpringExt implements Extension {
        private volatile ApplicationContext applicationContext;
 
        public void initialize(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        /**
         * Create a Props for the specified actorBeanName using the
         * SpringActorProducer class.
         *
         * @param actorBeanName The name of the actor bean to create Props for
         * @return a Props that will create the named actor bean using Spring
         */
        public Props create(String actorBeanName) {
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
        }

        /**
         * Create a Props for the specified actorBeanName using the
         * SpringActorProducer class.
         *
         * @param requiredType Type of the actor bean must match. Can be an interface or superclass
         *                     of the actual class, or {@code null} for any match. For example, if the value
         *                     is {@code Object.class}, this method will succeed whatever the class of the
         *                     returned instance.
         * @return a Props that will create the actor bean using Spring
         */
        public Props create(Class<? > requiredType) {
            return Props.create(SpringActorProducer.class, applicationContext, requiredType);
        }

        /**
         * Create a Props for the specified actorBeanName using the
         * SpringActorProducer class.
         *
         * @param actorBeanName The name of the actor bean to create Props for
         * @param requiredType  Type of the actor bean must match. Can be an interface or superclass
         *                      of the actual class, or {@code null} for any match. For example, if the value
         *                      is {@code Object.class}, this method will succeed whatever the class of the
         *                      returned instance.
         * @return a Props that will create the actor bean using Spring
         */
        public Props create(String actorBeanName, Class<? extends AbstractActor> requiredType) {
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanName, requiredType);
        }
    }
}