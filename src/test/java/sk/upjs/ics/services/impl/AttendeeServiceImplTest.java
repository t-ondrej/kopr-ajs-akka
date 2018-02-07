package sk.upjs.ics.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import sk.upjs.ics.entities.Attendee;
import sk.upjs.ics.entities.AttendanceSheet;
import sk.upjs.ics.services.AttendeeService;
import sk.upjs.ics.spring.config.TestConfig;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Tomas Ondrej
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class AttendeeServiceImplTest {

    @Autowired
    private AttendeeService attendeeService;

    @Test
    public void testRegisterValidAttendee() {
        Attendee attendee = new Attendee("First", "Second");
        attendeeService.registerAndGetId(attendee);
        assertNotNull(attendee.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterInvalidAttendee() {
        Attendee attendee = new Attendee("", "Second");
        attendeeService.registerAndGetId(attendee);
    }

    @Test
    public void testGetAttendedAttendanceSheets() {
        UUID attendeeId = UUID.fromString("a2000000-0000-0000-0000-000000000000");
        Set<AttendanceSheet> attendedAttendanceSheets = attendeeService.getAttendedAttendanceSheets(attendeeId);
        assertEquals(attendedAttendanceSheets.size(), 1);
    }

    @Test
    public void testGetZeroAttendedAttendanceSheets() {
        UUID attendeeId = UUID.fromString("a4000000-0000-0000-0000-000000000000");
        Set<AttendanceSheet> attendedAttendanceSheets = attendeeService.getAttendedAttendanceSheets(attendeeId);
        assertEquals(attendedAttendanceSheets.size(), 0);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAttendedAttendanceSheetAttendeeNotExist() {
        UUID attendeeId = UUID.fromString("a7000000-0000-0000-0000-000000000000");
        attendeeService.getAttendedAttendanceSheets(attendeeId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttendedAttendanceSheetsInvalidAttendeeId() {
        UUID attendeeId = null;
        Set<AttendanceSheet> attendedAttendanceSheets = attendeeService.getAttendedAttendanceSheets(attendeeId);
        assertEquals(attendedAttendanceSheets.size(), 1);
    }
}