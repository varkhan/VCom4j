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
     *
     */
    public EncodingException() {
        super();
    }

    /**
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public EncodingException(String message) {
        super(message);
    }

    /**
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@literal null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public EncodingException(Throwable cause) {
        super(cause);
    }

}
