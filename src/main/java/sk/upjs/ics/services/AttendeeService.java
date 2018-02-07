package sk.upjs.ics.services;

import sk.upjs.ics.entities.AttendanceSheet;
import sk.upjs.ics.entities.Attendee;

import java.util.Set;
import java.util.UUID;

/**
 * @author Tomas Ondrej
 */
public interface AttendeeService {
    /**
     * Persist attendee
     * @param attendee Attendee which should be persisted
     * @return UUID of the successfully persisted attendee
     */
    UUID registerAndGetId(Attendee attendee);

    /**
     * Retrieves attendance sheets where the attendee was registered
     * @param attendeeId UUID of the attendee we are finding attendance sheets for
     * @return Set of attended attendance sheets
     */
    Set<AttendanceSheet> getAttendedAttendanceSheets(UUID attendeeId);
}
