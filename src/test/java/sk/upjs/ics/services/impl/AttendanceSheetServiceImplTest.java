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
import sk.upjs.ics.services.AttendanceSheetService;
import sk.upjs.ics.spring.config.TestConfig;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Tomas Ondrej
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class AttendanceSheetServiceImplTest {

    @Autowired
    private AttendanceSheetService attendanceSheetService;

    @Test
    public void testSaveValidAttendanceSheet() {
        UUID lectureId = UUID.fromString("51000000-0000-0000-0000-000000000000");
        LocalDateTime dateTime = LocalDateTime.of(2018, 1, 1, 20, 0);
        List<UUID> attendeesUUIDs = Arrays.asList(UUID.fromString("a1000000-0000-0000-0000-000000000000"),
                                                  UUID.fromString("a2000000-0000-0000-0000-000000000000"));

        UUID attendanceSheetId = attendanceSheetService.saveAndGetId(lectureId, dateTime, attendeesUUIDs);
        assertNotNull(attendanceSheetId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testSaveAttendanceSheetLectureNotExist() {
        UUID lecture = UUID.fromString("12345678-0000-0000-0000-000000000000");
        LocalDateTime dateTime = LocalDateTime.of(2018, 1, 1, 20, 0);
        List<UUID> attendeesUUIDs = Collections.singletonList(UUID.fromString("a1000000-0000-0000-0000-000000000000"));

        attendanceSheetService.saveAndGetId(lecture, dateTime, attendeesUUIDs);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testSaveAttendanceSheetAttendeeNotExist() {
        UUID lectureId = UUID.fromString("51000000-0000-0000-0000-000000000000");
        LocalDateTime dateTime = LocalDateTime.of(2018, 1, 1, 20, 0);
        List<UUID> attendeesUUIDs = Collections.singletonList(UUID.fromString("f1000000-0000-0000-0000-000000000000"));

        attendanceSheetService.saveAndGetId(lectureId, dateTime, attendeesUUIDs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveInvalidAttendanceSheet() {
        UUID lectureId = UUID.fromString("51000000-0000-0000-0000-000000000000");
        LocalDateTime dateTime = null;
        List<UUID> attendeesUUIDs = Collections.singletonList(UUID.fromString("f1000000-0000-0000-0000-000000000000"));

        attendanceSheetService.saveAndGetId(lectureId, dateTime, attendeesUUIDs);
    }

    @Test
    public void testGetAttendanceSheetAttendeesFirstToLastNames() {
        UUID attendanceSheetId = UUID.fromString("d1000000-0000-0000-0000-000000000000");
        Map<String, String> firstToLastName = attendanceSheetService.getFirstToLastName(attendanceSheetId);
        Map.Entry<String,String> entry = firstToLastName.entrySet().iterator().next();
        String firstName = entry.getKey();
        String lastName = entry.getValue();

        assertEquals(firstToLastName.size(), 1);

        assertEquals(firstName, "First");
        assertEquals(lastName, "Surname");
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAttendanceSheetAttendeesFirstToLastNamesAttendanceSheetNotExist() {
        UUID attendanceSheetId = UUID.fromString("abc00000-0000-0000-0000-000000000000");
        attendanceSheetService.getFirstToLastName(attendanceSheetId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttendanceSheetAttendeesFirstToLastNamesInvalidAttendanceSheetId() {
        UUID attendanceSheetId = null;
        attendanceSheetService.getFirstToLastName(attendanceSheetId);
    }
}