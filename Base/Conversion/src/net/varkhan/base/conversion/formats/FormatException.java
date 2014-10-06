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
    protected final int ln;
    protected final int cn;
    protected final String cx;


    /**
     * Constructs a new format exception.
     * @param ln line number
     * @param cn char number
     * @param cx context string
     */
    public FormatException(int ln, int cn, String cx) {
        this.ln=ln;
        this.cn=cn;
        this.cx=cx;
    }

    /**
     * Constructs a new format exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *  @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param ln line number
     * @param cn char number
     * @param cx context string
     */
    public FormatException(String message, int ln, int cn, String cx) {
        super(message);
        this.ln=ln;
        this.cn=cn;
        this.cx=cx;
    }

    /**
     * Constructs a new format exception with the specified detail message and cause.
     *  @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param ln line number
     * @param cn char number
     * @param cx context string
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     */
    public FormatException(String message, int ln, int cn, String cx, Throwable cause) {
        super(message, cause);
        this.ln=ln;
        this.cn=cn;
        this.cx=cx;
    }

    /**
     * Constructs a new format exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * @param ln line number
     * @param cn char number
     * @param cx context string
     * @param cause the cause (which is saved for later retrieval by the
*              {@link #getCause()} method).  (A <tt>null</tt> value is
*              permitted, and indicates that the cause is nonexistent or
*              unknown.)
     */
    public FormatException(int ln, int cn, String cx, Throwable cause) {
        super(cause);
        this.ln=ln;
        this.cn=cn;
        this.cx=cx;
    }
}
