package sk.upjs.ics.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.upjs.ics.entities.AttendanceSheet;
import sk.upjs.ics.entities.Attendee;
import sk.upjs.ics.entities.Lecture;
import sk.upjs.ics.services.AttendanceSheetService;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of attendance sheet service which uses JPA entity manager as data provider interface
 *
 * @author Tomas Ondrej
 */
@Service
@Transactional
public class AttendanceSheetServiceImpl implements AttendanceSheetService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Persist attendance sheet
     * @param lectureId UUID of a lecture
     * @param dateTime Certain day and time of the attendance sheet
     * @param attendeesUUIDs UUIDs of all attendees that were registered on the attendance sheet
     * @return UUID of the persisted attendance sheet. UUID is generated by hibernate
     */
    public UUID saveAndGetId(UUID lectureId, LocalDateTime dateTime, List<UUID> attendeesUUIDs) {
        if (attendeesUUIDs.stream().anyMatch(attendeeId -> attendeeId == null)
                || lectureId == null || dateTime == null)
            throw new IllegalArgumentException("Invalid attendance sheet to be persisted.");

        Lecture lecture = em.getReference(Lecture.class, lectureId);

        try {
            lecture.toString();
        } catch (EntityNotFoundException exc) {
            throw new EntityNotFoundException("Subject with ID: " + lectureId.toString() + " not found.");
        }

         Set<Attendee> attendees = attendeesUUIDs
                .stream()
                .map(id -> em.getReference(Attendee.class, id))
                .collect(Collectors.toSet());

        attendees.forEach(attendee -> {
            try {
                attendee.toString();
            } catch (EntityNotFoundException exc) {
                throw new EntityNotFoundException("Attendee with ID: " + attendee.getId() + " not found.");
            }
        });

        AttendanceSheet attendanceSheet = new AttendanceSheet(dateTime, lecture, attendees);
        em.persist(attendanceSheet);

        return attendanceSheet.getId();
    }

    /**
     * Gets first and last name of registered attendees of specific attendance sheet
     * @param attendanceSheetId UUID of attendance sheet
     * @return Map which key is first name and value is last name of
     *         registered attendees on attendance sheet
     */
    public Map<String, String> getFirstToLastName(UUID attendanceSheetId) {
        if (attendanceSheetId == null)
            throw new IllegalArgumentException("Invalid attendance sheet id.");

        AttendanceSheet attendanceSheet = em.find(AttendanceSheet.class, attendanceSheetId);

        if (attendanceSheet == null)
            throw new EntityNotFoundException("Attendance sheet with ID: " + attendanceSheetId.toString() + " not found.");

        return attendanceSheet
                .getAttendees()
                .stream()
                .collect(Collectors.toMap(Attendee::getFirstName, Attendee::getLastName));
    }
}
