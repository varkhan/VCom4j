package net.varkhan.base.conversion.formats;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/6/12
 * @time 4:33 PM
 */
public class FormatException extends RuntimeException {
    /**
     * Constructs a new format exception.
     */
    public FormatException() {
    }

    /**
     * Constructs a new format exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public FormatException(String message) {
        super(message);
    }

    /**
     * Constructs a new format exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     *
     * @since 1.4
     */
    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new format exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     *
     * @since 1.4
     */
    public FormatException(Throwable cause) {
        super(cause);
    }
}
