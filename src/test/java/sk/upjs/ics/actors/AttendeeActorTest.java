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
public class AttendeeActorTest  {

    @Autowired
    private ActorRef attendeeActorRef;
    private static ActorSystem actorSystem;
    private static TestKit probe;

    @Autowired
    public void setActorSystem(ActorSystem actorSystem){
        AttendeeActorTest.actorSystem = actorSystem;
        probe = new TestKit(actorSystem);
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }

    @Test
    public void testSuccessfullyRegistered() {
        attendeeActorRef.tell(new AttendeeActor.RegisterAttendee(1, "TestName", "TestSurname"), probe.getRef());
        AttendeeActor.AttendeeRegistered response = probe.expectMsgClass(AttendeeActor.AttendeeRegistered.class);
        assertEquals(1, response.requestId);
        assertNotNull(response.attendeeId);
    }

    @Test
    public void testUnsuccessfullyRegistered() {
        attendeeActorRef.tell(new AttendeeActor.RegisterAttendee(2, "", "TestSurname"), probe.getRef());
        RespondInvalidEntityToPersist response = probe.expectMsgClass(RespondInvalidEntityToPersist.class);
        assertEquals(2, response.requestId);
    }

    @Test
    public void testGetAttendedAttendanceSheets() {
        UUID attendeeId = UUID.fromString("a2000000-0000-0000-0000-000000000000");
        attendeeActorRef.tell(new AttendeeActor.FindAttendanceSheetsOfAttendee(1, attendeeId), probe.getRef());
        AttendeeActor.RespondAttendanceSheetsOfAttendee response = probe.expectMsgClass(AttendeeActor.RespondAttendanceSheetsOfAttendee.class);
        assertEquals(1, response.requestId);
        assertEquals(response.attendanceSheetsOfAttendee.size(), 1);
    }

    @Test
    public void testGetZeroAttendedAttendanceSheets() {
        UUID attendeeId = UUID.fromString("a4000000-0000-0000-0000-000000000000");
        attendeeActorRef.tell(new AttendeeActor.FindAttendanceSheetsOfAttendee(2, attendeeId), probe.getRef());
        AttendeeActor.RespondAttendanceSheetsOfAttendee response = probe.expectMsgClass(AttendeeActor.RespondAttendanceSheetsOfAttendee.class);
        assertEquals(2, response.requestId);
        assertEquals(response.attendanceSheetsOfAttendee.size(), 0);
    }

    @Test
    public void testGetAttendedAttendanceSheetsAttendeeNotExist() {
        UUID attendeeId = UUID.fromString("a7000000-0000-0000-0000-000000000000");
        attendeeActorRef.tell(new AttendeeActor.FindAttendanceSheetsOfAttendee(3, attendeeId), probe.getRef());
        RespondEntityNotFound response = probe.expectMsgClass(RespondEntityNotFound.class);
        assertEquals(3, response.requestId);
    }

    @Test
    public void testGetAttendedAttendanceSheetsInvalidAttendeeId() {
        UUID attendeeId = null;
        attendeeActorRef.tell(new AttendeeActor.FindAttendanceSheetsOfAttendee(4, attendeeId), probe.getRef());
        RespondIllegalArgument response = probe.expectMsgClass(RespondIllegalArgument.class);
        assertEquals(4, response.requestId);
    }
}