package sk.upjs.ics.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import sk.upjs.ics.globalmessages.RespondEntityNotFound;
import sk.upjs.ics.globalmessages.RespondIllegalArgument;
import sk.upjs.ics.globalmessages.RespondInvalidEntityToPersist;
import sk.upjs.ics.spring.config.TestConfig;

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
public class AttendanceSheetActorTest {

    @Autowired
    private ActorRef attendanceSheetActorRef;
    private static ActorSystem actorSystem;
    private static TestKit probe;

    @Autowired
    public void setActorSystem(ActorSystem actorSystem){
        AttendanceSheetActorTest.actorSystem = actorSystem;
        probe = new TestKit(actorSystem);
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }

    @Test
    public void testSucessfulySavedAttendanceSheet() {
        UUID lectureId = UUID.fromString("51000000-0000-0000-0000-000000000000");
        LocalDateTime dateTime = LocalDateTime.of(2018, 1, 1, 20, 0);
        List<UUID> attendeesUUIDs = Arrays.asList(UUID.fromString("a1000000-0000-0000-0000-000000000000"),
                                                  UUID.fromString("a2000000-0000-0000-0000-000000000000"));
        attendanceSheetActorRef.tell(new AttendanceSheetActor.SaveAttendanceSheet(1, lectureId, dateTime, attendeesUUIDs), probe.getRef());
        AttendanceSheetActor.AttendanceSheetSaved response = probe.expectMsgClass(AttendanceSheetActor.AttendanceSheetSaved.class);
        assertEquals(1, response.requestId);
        assertNotNull(response.attendanceSheetId);
    }

    @Test
    public void testUnsuccessfullySavedAttendanceSheet() {
        UUID lectureId = UUID.fromString("51000000-0000-0000-0000-000000000000");
        LocalDateTime dateTime = null;
        List<UUID> attendeesUUIDs = Arrays.asList(UUID.fromString("a1000000-0000-0000-0000-000000000000"),
                UUID.fromString("a2000000-0000-0000-0000-000000000000"));
        attendanceSheetActorRef.tell(new AttendanceSheetActor.SaveAttendanceSheet(2, lectureId, dateTime, attendeesUUIDs), probe.getRef());
        RespondInvalidEntityToPersist response = probe.expectMsgClass(RespondInvalidEntityToPersist.class);
        assertEquals(2, response.requestId);
    }

    @Test
    public void testSaveAttendanceSheetLectureNotExist() {
        UUID lectureId = UUID.fromString("a1000000-0000-0000-0000-000000000000");
        LocalDateTime dateTime = LocalDateTime.of(2018, 1, 1, 20, 0);
        List<UUID> attendeesUUIDs = Arrays.asList(UUID.fromString("a1000000-0000-0000-0000-000000000000"),
                UUID.fromString("a2000000-0000-0000-0000-000000000000"));
        attendanceSheetActorRef.tell(new AttendanceSheetActor.SaveAttendanceSheet(3, lectureId, dateTime, attendeesUUIDs), probe.getRef());
        RespondEntityNotFound response = probe.expectMsgClass(RespondEntityNotFound.class);
        assertEquals(3, response.requestId);
    }

    @Test
    public void testSaveAttendanceSheetAttendeeNotExist() {
        UUID lectureId = UUID.fromString("51000000-0000-0000-0000-000000000000");
        LocalDateTime dateTime = LocalDateTime.of(2018, 1, 1, 20, 0);
        List<UUID> attendeesUUIDs = Collections.singletonList(UUID.fromString("11000000-0000-0000-0000-000000000000"));
        attendanceSheetActorRef.tell(new AttendanceSheetActor.SaveAttendanceSheet(4, lectureId, dateTime, attendeesUUIDs), probe.getRef());
        RespondEntityNotFound response = probe.expectMsgClass(RespondEntityNotFound.class);
        assertEquals(4, response.requestId);
    }

    @Test
    public void testGetAttendanceSheetAttendeesFullnames() {
        UUID attendanceSheetId = UUID.fromString("d1000000-0000-0000-0000-000000000000");
        attendanceSheetActorRef.tell(new AttendanceSheetActor.FindAttendanceSheetRegisteredFullnames(1, attendanceSheetId), probe.getRef());
        AttendanceSheetActor.RespondAttendanceSheetAttendeesFullnames response = probe.expectMsgClass(AttendanceSheetActor.RespondAttendanceSheetAttendeesFullnames.class);
        assertEquals(1, response.requestId);

        Map.Entry<String,String> entry = response.attendeesFirstToLastNames.entrySet().iterator().next();
        String firstName = entry.getKey();
        String lastName = entry.getValue();

        assertEquals(response.attendeesFirstToLastNames.size(), 1);

        assertEquals(firstName, "First");
        assertEquals(lastName, "Surname");
    }

    @Test
    public void testGetAttendanceSheetAttendeesFullnamesAttendanceSheetNotExist() {
        UUID attendanceSheetId = UUID.fromString("abc00000-0000-0000-0000-000000000000");
        attendanceSheetActorRef.tell(new AttendanceSheetActor.FindAttendanceSheetRegisteredFullnames(2, attendanceSheetId), probe.getRef());
        RespondEntityNotFound response = probe.expectMsgClass(RespondEntityNotFound.class);
        assertEquals(2, response.requestId);
    }

    @Test
    public void testGetAttendanceSheetAttendeesFullnamesInvalidId() {
        UUID attendanceSheetId = null;
        attendanceSheetActorRef.tell(new AttendanceSheetActor.FindAttendanceSheetRegisteredFullnames(3, attendanceSheetId), probe.getRef());
        RespondIllegalArgument response = probe.expectMsgClass(RespondIllegalArgument.class);
        assertEquals(3, response.requestId);
    }
}