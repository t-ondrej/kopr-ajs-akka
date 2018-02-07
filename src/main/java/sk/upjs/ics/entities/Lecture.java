package sk.upjs.ics.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Represents lecture
 *
 * @author Tomas Ondrej
 */
@Entity
@Table(name="Lecture")
public class Lecture extends AbstractEntity {

    /**
     * Subject of the lecture
     */
    private Subject subject;

    /**
     * Attendance sheet of the lecture
     */
    private Set<AttendanceSheet> attendanceSheets;

    /**
     * Empty constructor to create new instance by
     * persistence framework
     */
    public Lecture() {
        // Placeholder
    }

    public Lecture(Subject subject) {
        this.subject = subject;
    }

    /**
     * Checks if lecture is valid
     * @return True if subject is valid
     */
    @Transient
    @Override public boolean isValid() {
        return subject.isValid();
    }

    @OneToOne(cascade=CascadeType.ALL)
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    public Set<AttendanceSheet> getAttendanceSheets() {
        return attendanceSheets;
    }

    public void setAttendanceSheets(Set<AttendanceSheet> attendanceSheets) {
        this.attendanceSheets = attendanceSheets;
    }
}
