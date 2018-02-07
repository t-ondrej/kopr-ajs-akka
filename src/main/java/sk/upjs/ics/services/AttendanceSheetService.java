package sk.upjs.ics.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Tomas Ondrej
 */
public interface AttendanceSheetService {
    /**
     * Persist attendance sheet
     * @param lectureId UUID of the lecture which we are creating attendance sheet for
     * @param dateTime Certain day and time of the attendance sheet
     * @param attendeesUUIDs UUIDs of all attendees that were registered on the attendance sheet
     * @return UUID of attendance sheet if it was successfully persisted
     */
    UUID saveAndGetId(UUID lectureId, LocalDateTime dateTime, List<UUID> attendeesUUIDs);

    /**
     * Gets first and last name of registered attendees of specific attendance sheet
     * @param attendanceSheetId UUID of attendance sheet
     * @return Map of first to last name of attendees that were on the attendance sheet
     */
    Map<String, String> getFirstToLastName(UUID attendanceSheetId);
}
