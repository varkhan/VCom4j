/**
 *
 */
package net.varkhan.base.management.logging;

import java.io.Flushable;


/**
 * <b>A log event writer.</b>
 * <p/>
 * Dispatch log events to a central location (file, stream, service or other storage location).
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 10:17:48 PM
 *
 * @param <T> the event content type
 */
public interface LogWriter<T> extends Flushable {

    /**
     * Create and dispatch a log event, with a event weight of {@literal 1}.
     *
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param tms the event time-stamp (in milliseconds)
     * @param msg the event content
     */
    public void log(String ctx, String key, int lev, long tms, T msg);

    /**
     * Create and dispatch a log event.
     *
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param tms the event time-stamp (in milliseconds)
     * @param val the event weight
     * @param msg the event content
     */
    public void log(String ctx, String key, int lev, long tms, double val, T msg);

    /**
     * Dispatch a log event.
     *
     * @param evt the event
     */
    public void log(LogEvent<T> evt);

    /**
     * Dispatch a collection of log events.
     *
     * @param evts the events
     */
    public void log(Iterable<LogEvent<T>> evts);

    /**
     * Finish dispatching any pending event.
     */
    public void flush();

}
