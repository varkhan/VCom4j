/**
 *
 */
package net.varkhan.base.conversion.serializer;

/**
 * <b>.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 7, 2010
 * @time 11:45:36 PM
 */
public class SerializationException extends RuntimeException {
    private static final long serialVersionUID=1L;

    /**
     * Constructs a new serialization exception.
     */
    public SerializationException() {
        super();
    }

    /**
     * Constructs a new serialization exception.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new serialization exception.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public SerializationException(String message) {
        super(message);
    }

    /**
     * Constructs a new serialization exception.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public SerializationException(Throwable cause) {
        super(cause);
    }


}
