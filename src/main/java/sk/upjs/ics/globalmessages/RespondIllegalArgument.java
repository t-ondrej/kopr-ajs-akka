package sk.upjs.ics.globalmessages;

/**
 * Response message if the argument in request was illegal
 *
 * @author Tomas Ondrej
 */
public final class RespondIllegalArgument {

    public final long requestId;
    public final String message;

    /**
     * Constructs response message to illegal argument event
     * @param requestId Id of the request
     * @param message Message of the response
     */
    public RespondIllegalArgument(long requestId, String message) {
        this.requestId = requestId;
        this.message = message;
    }
}
