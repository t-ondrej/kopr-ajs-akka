package sk.upjs.ics.services;

import sk.upjs.ics.entities.Lecture;

import java.util.UUID;

/**
 * @author Tomas Ondrej
 */
public interface LectureService {
    /**
     * Persist lecture
     * @param lecture Lecture which should be persisted
     * @return UUID of successfully saved lecture
     */
    UUID saveAndGetId(Lecture lecture);
}
