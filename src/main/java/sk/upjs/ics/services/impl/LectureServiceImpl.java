package sk.upjs.ics.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.upjs.ics.entities.Lecture;
import sk.upjs.ics.services.LectureService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Implementation of lecture service which uses JPA entity manager as data provider interface
 *
 * @author Tomas Ondrej
 */
@Service
@Transactional
public class LectureServiceImpl implements LectureService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Persist lecture
     * @param lecture Lecture to be persisted
     * @return UUID of the persisted lecture. UUID is generated by hibernate
     */
    public UUID saveAndGetId(Lecture lecture) {
        if (lecture == null || !lecture.isValid())
            throw new IllegalArgumentException("Invalid lecture to be saved.");

        em.persist(lecture);
        return lecture.getId();
    }
}