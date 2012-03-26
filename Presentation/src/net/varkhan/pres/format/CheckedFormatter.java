package net.varkhan.pres.format;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/9/11
 * @time 7:15 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class CheckedFormatter implements Formatter {

    private final Appendable out;
    protected boolean opened = false;
    protected boolean closed = false;

    public CheckedFormatter(Appendable out) { this.out=out; }


    /**
     * Checks whether the formatter has been opened.
     *
     * @return {@literal true} if {@link #open()} has already been called
     */
    public boolean isOpen() { return opened; }

    /**
     * Opens the formatter.
     *
     * @throws IllegalStateException if the formatter was already opened
     * @throws IOException if the output Appendable generated an exception
     */
    public void open() throws IOException, IllegalStateException {
        if(opened) throw new IllegalStateException("Formatter is already opened");
        opened=true;
    }

    public CheckedFormatter append(CharSequence csq) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        out.append(csq);
        return this;
    }

    public CheckedFormatter append(CharSequence csq, int start, int end) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        out.append(csq, start, end);
        return this;
    }

    public CheckedFormatter append(char c) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        out.append(c);
        return this;
    }

    public void flush() throws IOException { if(out instanceof Flushable) ((Flushable) out).flush(); }

    /**
     * Checks whether the formatter has been closed.
     *
     * @return {@literal true} if {@link #close()} has already been called
     */
    public boolean isClosed() { return closed; }

    /**
     * Closes the formatter.
     *
     * @throws IllegalStateException if the formatter was already closed
     * @throws IOException if the output Appendable generated an exception
     */
    public void close() throws IOException, IllegalStateException {
        if(closed) throw new IllegalStateException("Formatter is already closed");
        if(out instanceof Flushable) ((Flushable) out).flush();
//        if(out instanceof Closeable) ((Closeable) out).close();
        closed=true;
    }
}
