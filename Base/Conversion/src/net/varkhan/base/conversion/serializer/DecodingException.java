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
public class DecodingException extends SerializationException {
    private static final long serialVersionUID=1L;

    /**
     * Constructs a new decoding exception.
     */
    public DecodingException() {
        super();
    }

    /**
     * Constructs a new decoding exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new decoding exception.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public DecodingException(String message) {
        super(message);
    }

    /**
     * Constructs a new decoding exception.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DecodingException(Throwable cause) {
        super(cause);
    }


}
