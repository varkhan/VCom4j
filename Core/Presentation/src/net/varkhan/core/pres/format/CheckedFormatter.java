package net.varkhan.core.pres.format;

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

    protected final Appendable out;
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
        if(csq!=null) out.append(csq);
        return this;
    }

    public CheckedFormatter append(CharSequence csq, int beg, int end) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        if(csq!=null) out.append(csq, beg, end);
        return this;
    }

    public CheckedFormatter append(char c) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        out.append(c);
        return this;
    }

    public CheckedFormatter append(char[] car) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        for(char c: car) out.append(c);
        return this;
    }

    public CheckedFormatter append(char[] car, int beg, int len) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        final int end = beg+len;
        if(beg<0 || beg>=car.length) throw new ArrayIndexOutOfBoundsException(beg);
        if(end<beg || end>car.length) throw new ArrayIndexOutOfBoundsException(end);
        for(int i=beg;i<end;i++) out.append(car[beg]);
        return this;
    }

    public CheckedFormatter ln() throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        out.append('\n');
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
