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
import sk.upjs.ics.globalmessages.RespondInvalidEntityToPersist;
import sk.upjs.ics.spring.config.TestConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Tomas Ondrej
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class LectureActorTest {

    @Autowired
    private ActorRef lectureActorRef;
    private static ActorSystem actorSystem;
    private static TestKit probe;

    @Autowired
    public void setActorSystem(ActorSystem actorSystem){
        LectureActorTest.actorSystem = actorSystem;
        probe = new TestKit(actorSystem);
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }

    @Test
    public void testSuccessfullySavedLecture() {
        lectureActorRef.tell(new LectureActor.SaveLecture(1, "Biology"), probe.getRef());
        LectureActor.LectureSaved response = probe.expectMsgClass(LectureActor.LectureSaved.class);
        assertEquals(1, response.requestId);
        assertNotNull(response.lectureId);
    }

    @Test
    public void testUnsuccessfullySavedLecture() {
        lectureActorRef.tell(new LectureActor.SaveLecture(2, ""), probe.getRef());
        RespondInvalidEntityToPersist response = probe.expectMsgClass(RespondInvalidEntityToPersist.class);
        assertEquals(2, response.requestId);
    }
}