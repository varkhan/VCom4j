package net.varkhan.pres.format;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;


/**
 * <b>An object that provides methods to <i>format</i> objects and char sequences.</b>.
 * <p/>
 * This interface does not define any specific formatting, beyond the methods
 * defined in {@link Appendable}. Subclasses can extend those to provide
 * functionality beyond these core methods.
 * <p/>
 * A Formatter has a life cycle: it must be opened before receiving input, and closed
 * after all input has been received. No input is accepted while not open, or closed.
 * <p/>
 *
 * @author varkhan
 * @date 1/9/11
 * @time 7:12 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public interface Formatter extends Appendable, Flushable, Closeable {

    /**
     * Checks whether the formatter has been opened.
     *
     * @return {@literal true} if {@link #open()} has already been called
     */
    public boolean isOpen();

    /**
     * Opens the formatter.
     *
     * @throws IOException if the output Appendable generated an exception
     */
    public void open() throws IOException;

    public Formatter append(CharSequence csq) throws IOException;

    public Formatter append(CharSequence csq, int start, int end) throws IOException;

    public Formatter append(char c) throws IOException;

    public void flush() throws IOException;

    /**
     * Checks whether the formatter has been closed.
     *
     * @return {@literal true} if {@link #close()} has already been called
     */
    public boolean isClosed();

    /**
     * Closes the formatter.
     *
     * @throws IOException if the output Appendable generated an exception
     */
    public void close() throws IOException;

}
