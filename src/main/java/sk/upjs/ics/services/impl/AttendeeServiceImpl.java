package sk.upjs.ics.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.upjs.ics.entities.AttendanceSheet;
import sk.upjs.ics.entities.Attendee;
import sk.upjs.ics.services.AttendeeService;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of attendee service which uses JPA entity manager as data provider interface
 *
 * @author Tomas Ondrej
 */
@Service
@Transactional
public class AttendeeServiceImpl implements AttendeeService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Persist attendee
     * @param attendee Attendee which should be persisted
     * @return UUID of the persisted attendee. UUID is generated by hibernate
     */
    public UUID registerAndGetId(Attendee attendee) {
        if (attendee == null || !attendee.isValid())
            throw new IllegalArgumentException("Invalid attendee to be registered.");

        em.persist(attendee);

        return attendee.getId();
    }

    /**
     * Retrieves attendance sheets where the attendee was registered
     * @param attendeeId UUID of the attendee we are finding attendance sheets for
     * @return Set of attended attendance sheets
     */
    public Set<AttendanceSheet> getAttendedAttendanceSheets(UUID attendeeId) {
        if (attendeeId == null)
            throw new IllegalArgumentException("Attendee ID must not be null.");

        Attendee attendee = em.find(Attendee.class, attendeeId);

        if (attendee == null)
            throw new EntityNotFoundException("Attendee with ID: " + attendeeId.toString() + " not found.");

           return attendee.getAttendanceSheets();
    }
}
