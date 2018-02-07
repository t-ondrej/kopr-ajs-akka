package sk.upjs.ics.globalmessages;

/**
 * Response message if the entity we tried to persist was invalid
 *
 * @author Tomas Ondrej
 */
public class RespondInvalidEntityToPersist {

    public final long requestId;
    public final String message;

    /**
     * Constructs response message to invalid entity to persist event
     * @param requestId Id of the request
     * @param message Message of the response
     */
    public RespondInvalidEntityToPersist(long requestId, String message) {
        this.requestId = requestId;
        this.message = message;
    }
}
