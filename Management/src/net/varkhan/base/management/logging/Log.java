/**
 *
 */
package net.varkhan.base.management.logging;

/**
 * <b>A configured log event handler.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 11:05:34 PM
 *
 * @param <T> the event content type
 */
public interface Log<T> {

    /**
     * Return the logging context.
     *
     * @return the current logging context
     */
    public String getContext();

    /**
     * Return the logging key.
     *
     * @return the current logging key
     */
    public String getKey();

    /**
     * Return the logging level mask.
     *
     * @return the enabled severity level mask for the current context and key
     */
    public long getLevelMask();

    /**
     * Indicate whether a particular logging level is enabled.
     *
     * @param lev a severity level
     *
     * @return {@code true} iff logging for the severity level {@code lev} is enabled, for the current context and key
     */
    public boolean isLevelEnabled(int lev);

    /**
     * Create and dispatch a log event, with a event weight of {@literal 1}.
     *
     * @param lev a severity level
     * @param msg the event content
     */
    public void log(int lev, T msg);

    /**
     * Create and dispatch a log event.
     *
     * @param lev a severity level
     * @param val the event weight
     * @param msg the event content
     */
    public void log(int lev, double val, T msg);

}
