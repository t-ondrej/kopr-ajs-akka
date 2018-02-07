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
import sk.upjs.ics.entities.Lecture;
import sk.upjs.ics.entities.Subject;
import sk.upjs.ics.services.LectureService;
import sk.upjs.ics.spring.config.TestConfig;

import static org.junit.Assert.assertNotNull;

/**
 * @author Tomas Ondrej
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class LectureServiceImplTest {

    @Autowired
    private LectureService lectureService;

    @Test
    public void testRegisterValidAttendee() {
        Lecture lecture = new Lecture(new Subject("Math"));
        lectureService.saveAndGetId(lecture);
        assertNotNull(lecture.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterInvalidAttendee() {
        Lecture lecture = new Lecture(new Subject(""));
        lectureService.saveAndGetId(lecture);
    }
}