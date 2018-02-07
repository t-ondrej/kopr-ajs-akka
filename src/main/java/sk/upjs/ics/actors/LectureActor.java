package sk.upjs.ics.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import sk.upjs.ics.entities.Lecture;
import sk.upjs.ics.entities.Subject;
import sk.upjs.ics.globalmessages.RespondInvalidEntityToPersist;
import sk.upjs.ics.services.LectureService;
import sk.upjs.ics.spring.akka.Actor;

import java.util.UUID;

/**
 * Actor which handles lecture related requests
 *
 * @author Tomas Ondrej
 */
@Actor
public class LectureActor extends AbstractActor {

    @Autowired
    private LectureService       lectureService;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     * Save lecture request message
     */
    public static final class SaveLecture {
        final long   requestId;
        final String subjectName;

        /**
         * Constructs save lecture request message which contains subject's name
         * @param requestId Request's Id
         * @param subjectName Subject's name
         */
        public SaveLecture(long requestId, String subjectName) {
            this.requestId = requestId;
            this.subjectName = subjectName;
        }
    }

    /**
     * Save lecture response message
     */
    public static final class LectureSaved {
        final long requestId;
        final UUID lectureId;

        /**
         * Constructs save lecture response message with saved lecture's id
         * @param requestId Request's id
         * @param lectureId Lecture's UUID
         */
        public LectureSaved(long requestId, UUID lectureId) {
            this.requestId = requestId;
            this.lectureId = lectureId;
        }
    }

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
                .match(SaveLecture.class, r -> {
                    log.info("Received save lecture request {}", r.requestId);
                    try {
                        UUID lectureId = lectureService.saveAndGetId(new Lecture(new Subject(r.subjectName)));
                        getSender().tell(new LectureSaved(r.requestId, lectureId), getSelf());
                    } catch (IllegalArgumentException exc) {
                        getSender().tell(new RespondInvalidEntityToPersist(r.requestId, exc.getMessage()), getSelf());
                    }
                })
                .matchAny(this::unhandled)
                .build();
    }
}
