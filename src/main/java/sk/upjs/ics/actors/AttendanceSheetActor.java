package sk.upjs.ics.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import sk.upjs.ics.globalmessages.RespondEntityNotFound;
import sk.upjs.ics.globalmessages.RespondIllegalArgument;
import sk.upjs.ics.globalmessages.RespondInvalidEntityToPersist;
import sk.upjs.ics.services.AttendanceSheetService;
import sk.upjs.ics.spring.akka.Actor;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Actor which handles attendance sheet related requests
 *
 * @author Tomas Ondrej
 */
@Actor
public class AttendanceSheetActor extends AbstractActor {

    @Autowired
    private AttendanceSheetService attendanceSheetService;
    private final LoggingAdapter   log = Logging.getLogger(getContext().system(), this);

    /**
     * Save attendance sheet request message
     */
    public static final class SaveAttendanceSheet {
        final long          requestId;
        final UUID          lectureId;
        final LocalDateTime dateTime;
        final List<UUID>    attendeesIds;

        /**
         * Constructs request message to save attendance sheet
         * @param requestId Id of the request
         * @param lectureId Id of the lecture
         * @param dateTime Day and time of the attendance sheet
         * @param attendeesIds UUIDs of registered attendees
         */
        public SaveAttendanceSheet(long requestId, UUID lectureId, LocalDateTime dateTime, List<UUID> attendeesIds) {
            this.requestId = requestId;
            this.lectureId = lectureId;
            this.dateTime = dateTime;
            this.attendeesIds = attendeesIds;
        }
    }

    /**
     * Response to save attendance sheet request message
     */
    public static final class AttendanceSheetSaved {
        final long requestId;
        final UUID attendanceSheetId;

        /**
         * Constructs response to save attendance sheet request message
         * @param requestId Id of the request
         * @param attendanceSheetId Id of the saved attendance sheet
         */
        public AttendanceSheetSaved(long requestId, UUID attendanceSheetId) {
            this.requestId = requestId;
            this.attendanceSheetId = attendanceSheetId;
        }
    }

    /**
     * Find registered full names of attendance sheet request message
     */
    public static final class FindAttendanceSheetRegisteredFullnames {
        final long requestId;
        final UUID attendanceSheetId;

        /**
         * Constructs request message to find registered full names of attendance sheet
         * @param requestId Id of the request
         * @param attendanceSheetId UUID of the attendance sheet
         */
        public FindAttendanceSheetRegisteredFullnames(long requestId, UUID attendanceSheetId) {
            this.requestId = requestId;
            this.attendanceSheetId = attendanceSheetId;
        }
    }

    /**
     * Response to find registered full names of attendance sheet request
     */
    public static final class RespondAttendanceSheetAttendeesFullnames {
        final long                  requestId;
        final UUID                  attendanceSheetId;
        final Map<String, String>   attendeesFirstToLastNames;

        /**
         * Constructs response to find registered full names of attendance sheet request
         * @param requestId Id of the request
         * @param attendanceSheetId UUID of the attendee sheet
         * @param attendeesFirstToLastNames Map which key is first name and value is last name of registered attendees
         */
        public RespondAttendanceSheetAttendeesFullnames(long requestId, UUID attendanceSheetId, Map<String, String> attendeesFirstToLastNames) {
            this.requestId = requestId;
            this.attendanceSheetId = attendanceSheetId;
            this.attendeesFirstToLastNames = attendeesFirstToLastNames;
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
                .match(SaveAttendanceSheet.class, r -> {
                    log.info("Received save attendance sheet request {}", r.requestId);
                    try {
                        UUID attendanceSheetId = attendanceSheetService.saveAndGetId(r.lectureId, r.dateTime, r.attendeesIds);
                        getSender().tell(new AttendanceSheetSaved(r.requestId, attendanceSheetId), getSelf());
                    } catch (IllegalArgumentException exc) {
                        getSender().tell(new RespondInvalidEntityToPersist(r.requestId, exc.getMessage()), getSelf());
                    } catch (EntityNotFoundException exc) {
                        getSender().tell(new RespondEntityNotFound(r.requestId, exc.getMessage()), getSelf());
                    }
                })
                .match(FindAttendanceSheetRegisteredFullnames.class, r -> {
                    log.info("Received find registered full names of attendance sheet request {}", r.requestId);
                    try {
                        Map<String, String> firstToLastName = attendanceSheetService.getFirstToLastName(r.attendanceSheetId);
                        getSender().tell(new RespondAttendanceSheetAttendeesFullnames(r.requestId, r.attendanceSheetId, firstToLastName),
                                        getSelf());
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
