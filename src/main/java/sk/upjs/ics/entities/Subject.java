package sk.upjs.ics.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Represents subject
 *
 * @author Tomas Ondrej
 */
@Entity
@Table(name="Subject")
public class Subject extends AbstractEntity {

    /**
     * Name of the subject
     */
    private String name;

    /**
     * Empty constructor to create new instance by
     * persistence framework
     */
    public Subject() {
        // Placeholder
    }

    public Subject(String name) {
        this.name = name;
    }

    @Transient
    @Override public boolean isValid() {
        return name != null && !name.trim().isEmpty();
    }

    @Column(name="Name", nullable=false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
