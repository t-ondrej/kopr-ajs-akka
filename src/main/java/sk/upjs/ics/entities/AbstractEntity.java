package sk.upjs.ics.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Base for every entity
 *
 * @author Tomas Ondrej
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    /**
     * UUID of the entity
     */
    protected UUID id;

    /**
     * Check if entity is valid
     * @return True if entity is valid
     */
    @Transient
    public abstract boolean isValid();

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
