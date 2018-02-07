package sk.upjs.ics.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import sk.upjs.ics.entities.AttendanceSheet;
import sk.upjs.ics.entities.Attendee;
import sk.upjs.ics.globalmessages.RespondEntityNotFound;
import sk.upjs.ics.globalmessages.RespondIllegalArgument;
import sk.upjs.ics.globalmessages.RespondInvalidEntityToPersist;
import sk.upjs.ics.services.AttendeeService;
import sk.upjs.ics.spring.akka.Actor;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.UUID;

/**
 * Actor which handles attendee related requests
 *
 * @author Tomas Ondrej
 */
@Actor
public class AttendeeActor extends AbstractActor {

    @Autowired
    private AttendeeService      attendeeService;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     *  Register attendee request message
     */
    public static final class RegisterAttendee {
        final long   requestId;
        final String firstName;
        final String lastName;

        /**
         * Constructs a register attendee request message with attendee's first and last name
         * @param requestId Id of the request
         * @param firstName Attendee's first name
         * @param lastName  Attendee's last name
         */
        public RegisterAttendee(long requestId, String firstName, String lastName) {
            this.requestId = requestId;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    /**
     * Response message to the register attendee request
     * Response is sent if and only if the attendee was successfully registered
     */
    public static final class AttendeeRegistered { 
        final long requestId;
        final UUID attendeeId;

        /**
         * Constructs response message to register attendee request
         * which contains registered attendee's UUID
         * @param requestId Id of the request
         * @param attendeeId UUID of the registered attendee
         */
        public AttendeeRegistered(long requestId, UUID attendeeId) {
            this.requestId = requestId;
            this.attendeeId = attendeeId;
        }
    }

    /**
     * Request message to find all attendance sheets where attendee is registered
     */
    public static final class FindAttendanceSheetsOfAttendee {
        final long requestId;
        final UUID attendeeId;

        /**
         * Constructs request to find all attendance sheets where attendee with the UUID is registered
         * @param requestId Id of the request
         * @param attendeeId UUID of the target attendee
         */
        public FindAttendanceSheetsOfAttendee(long requestId, UUID attendeeId) {
            this.requestId = requestId;
            this.attendeeId = attendeeId;
        }
    }

    /**
     * Response message to find attendance sheets where attendee is registered request
     */
    public static final class RespondAttendanceSheetsOfAttendee {
        final long                 requestId;
        final Set<AttendanceSheet> attendanceSheetsOfAttendee;

        /**
         * Constructs response message to find attendance sheets where attendee is registered request
         * @param requestId Id of the request
         * @param attendanceSheetsOfAttendee All attendance sheets where the attendee is registered
         */
        public RespondAttendanceSheetsOfAttendee(long requestId, Set<AttendanceSheet> attendanceSheetsOfAttendee) {
            this.requestId = requestId;
            this.attendanceSheetsOfAttendee = attendanceSheetsOfAttendee;
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

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RegisterAttendee.class, r -> {
                    log.info("Received register attendee request {}", r.requestId);
                    try {
                        UUID attendeeId = attendeeService.registerAndGetId(new Attendee(r.firstName, r.lastName));
                        getSender().tell(new AttendeeRegistered(r.requestId, attendeeId), getSelf());
                    } catch (IllegalArgumentException exc) {
                        getSender().tell(new RespondInvalidEntityToPersist(r.requestId, exc.getMessage()), getSelf());
                    }
                })
                .match(FindAttendanceSheetsOfAttendee.class, r -> {
                    log.info("Received find attendance sheets of attendee request {}", r.requestId);
                    try {
                        Set<AttendanceSheet> attendanceSheetsOfAttendee = attendeeService.getAttendedAttendanceSheets(r.attendeeId);
                        getSender().tell(new RespondAttendanceSheetsOfAttendee(r.requestId, attendanceSheetsOfAttendee), getSelf());
                    } catch (IllegalArgumentException exc) {
                        getSender().tell(new RespondIllegalArgument(r.requestId, exc.getMessage()), getSelf());
                    } catch (EntityNotFoundException exc) {
                        getSender().tell(new RespondEntityNotFound(r.requestId, exc.getMessage()), getSelf());
                    }
                })
                .matchAny(this::unhandled)
                .build();
    }
}
