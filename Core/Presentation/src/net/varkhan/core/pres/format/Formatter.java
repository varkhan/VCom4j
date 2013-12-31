package net.varkhan.core.pres.format;

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
     * @throws  IOException if an I/O error occurs
     */
    public void open() throws IOException;

    /**
     * Appends the specified character sequence to the Formatter.
     *
     * @param  csq the character sequence to append
     *
     * @return a reference to this Formatter
     * @throws  IOException if an I/O error occurs
     */
    public Formatter append(CharSequence csq) throws IOException;

    /**
     * Appends a subsequence of the specified character sequence to the Formatter.
     * <p/>
     * An invocation of this method of the form {@code fmt.append(csq, beg end)} when
     * {@code csq} is not {@code null}, behaves in exactly the same way as the invocation
     *
     * <pre> fmt.append(csq.subSequence(beg, end)) </pre>
     *
     * @param  csq the character sequence from which a subsequence will be appended.
     * @param  beg the index of the first character in the subsequence
     * @param  end the index of the character following the last character in the subsequence

     * @return a reference to this Formatter

     * @throws IOException if an I/O error occurs
     * @throws IndexOutOfBoundsException
     *          if {@code beg} or {@code end} are negative, {@code beg}
     *          is greater than {@code end}, or {@code end} is greater than
     *          {@code csq.length()}
     */
    public Formatter append(CharSequence csq, int beg, int end) throws IOException;

    /**
     * Appends the specified character to the Formatter.
     *
     * @param c the character to append
     * @return a reference to this Formatter
     * @throws IOException if an I/O error occurs
     */
    public Formatter append(char c) throws IOException;

    /**
     * Appends a sequence of characters to the Formatter.
     *
     * @param car the character array containing the sequence
     * @return a reference to this Formatter
     * @throws IOException if an I/O error occurs
     */
    public Formatter append(char[] car) throws IOException;

    /**
     * Appends a sequence characters to the Formatter.
     *
     * @param car the character array to append
     * @param beg the beginning of the sequence in the array
     * @param len the length of the sequence (number of characters to append)
     * @return a reference to this Formatter
     * @throws IOException if an I/O error occurs
     */
    public Formatter append(char[] car, int beg, int len) throws IOException;

    /**
     * Appends a newline to the formatter.
     *
     * @return a reference to this Formatter
     * @throws IOException if an I/O error occurs
     */
    public Formatter ln() throws IOException;

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
