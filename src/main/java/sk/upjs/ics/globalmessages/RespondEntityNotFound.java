package sk.upjs.ics.globalmessages;

/**
 * Response message if requested entity was not found
 *
 * @author Tomas Ondrej
 */
public final class RespondEntityNotFound {

    public final long requestId;
    public final String message;

    /**
     * Constructs response message to entity not found event
     * @param requestId Id of the request
     * @param message Message of the response
     */
    public RespondEntityNotFound(long requestId, String message) {
        this.requestId = requestId;
        this.message = message;
    }
}
