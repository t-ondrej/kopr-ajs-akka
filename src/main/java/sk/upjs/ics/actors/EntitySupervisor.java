package sk.upjs.ics.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;
import sk.upjs.ics.spring.akka.Actor;
import sk.upjs.ics.spring.akka.SpringExtension;

/**
 * Top level actor
 *
 * @author Tomas Ondrej
 */
@Actor
public class EntitySupervisor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef attendeeActor = getContext()
            .actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER
                        .get(getContext().getSystem())
                        .create(AttendeeActor.class)
                        .withRouter(new RoundRobinPool(3)),
                    "attendeeActor");

    private ActorRef lectureActor = getContext()
            .actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER
                        .get(getContext().getSystem())
                        .create(LectureActor.class)
                        .withRouter(new RoundRobinPool(3)),
                    "lectureActor");

    private ActorRef attendanceSheetActor = getContext()
            .actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER
                        .get(getContext().getSystem())
                        .create(AttendanceSheetActor.class)
                        .withRouter(new RoundRobinPool(3)),
                    "attendanceSheetActor");

    @Override
    public void preStart() {
        log.info("Started");
    }

    @Override
    public void postStop() {
        log.info("Stopped");
    }

    @Override public Receive createReceive() {
        return receiveBuilder()
                .build();
    }
}
