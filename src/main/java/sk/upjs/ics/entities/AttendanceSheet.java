package sk.upjs.ics.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Attendance sheet of certain lecture, day and time with registered attendees
 *
 * @author Tomas Ondrej
 */
@Entity
@Table(name="AttendanceSheet")
public class AttendanceSheet extends AbstractEntity {

    /**
     * Specific day and time of attendance sheet
     */
    private LocalDateTime dateTime;

    /**
     * Lecture which attendance sheet belongs to
     */
    private Lecture lecture;

    /**
     * Set of attendance sheet registered attendees
     */
    private Set<Attendee> attendees;

    /**
     * Empty constructor to create new instance by
     * persistence framework
     */
    public AttendanceSheet() {
        // Placeholder
    }

    /**
     * Constructs new attendance sheet
     * @param dateTime Certain day and time of the attendance sheet
     * @param lecture Lecture which attendance sheet belongs to
     * @param attendees List of attendance sheet attendees
     */
    public AttendanceSheet(LocalDateTime dateTime, Lecture lecture, Set<Attendee> attendees) {
        this.dateTime = dateTime;
        this.lecture = lecture;
        this.attendees = attendees;
    }

    /**
     * Check if attendance sheet is valid
     * @return True if datetime is not null, subject and every attendee is valid
     */
    @Transient
    @Override public boolean isValid() {
        return dateTime != null & lecture.isValid()
                && attendees.stream().allMatch(Attendee::isValid);
    }

    @Column(name="Day", nullable=false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @ManyToOne
    @JoinColumn(name = "Lecture_Id")
    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    @ManyToMany(mappedBy = "attendanceSheets")
    public Set<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<Attendee> attendees) {
        this.attendees = attendees;
    }


}
