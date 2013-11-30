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
 * @time 11:47:40 PM
 */
public class EncodingException extends SerializationException {
    private static final long serialVersionUID=1L;

    /**
     * Constructs a new encoding exception.
     */
    public EncodingException() {
        super();
    }

    /**
     * Constructs a new encoding exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new encoding exception.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public EncodingException(String message) {
        super(message);
    }

    /**
     * Constructs a new encoding exception.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public EncodingException(Throwable cause) {
        super(cause);
    }

}
