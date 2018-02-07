package sk.upjs.ics.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Represents attendee of lecture
 *
 * @author Tomas Ondrej
 */
@Entity
@Table(name="Attendee")
public class Attendee extends AbstractEntity {

    /**
     * Attendee's first name
     */
    private String firstName;

    /**
     * Attendee's last name
     */
    private String lastName;

    /**
     * Set of all attendance sheets where attendee is registered on
     */
    private Set<AttendanceSheet> attendanceSheets;

    /**
     * Empty constructor to create new instance by
     * persistence framework
     */
    public Attendee() {
        // Placeholder
    }

    public Attendee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks for attendee's validity
     * @return True if attendee's first and last name is valid
     */
    @Transient
    @Override public boolean isValid() {
        return firstName != null && lastName != null
                && !firstName.trim().isEmpty() && !lastName.trim().isEmpty();
    }

    @Column(name="Firstname", nullable=false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name="Lastname", nullable=false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(
            name = "AttendanceSheet_Attendee",
            joinColumns = { @JoinColumn(name = "Attendee_Id") },
            inverseJoinColumns = { @JoinColumn(name = "AttendanceSheet_Id") }
    )
    public Set<AttendanceSheet> getAttendanceSheets() {
        return attendanceSheets;
    }

    public void setAttendanceSheets(Set<AttendanceSheet> attendanceSheets) {
        this.attendanceSheets = attendanceSheets;
    }
}
