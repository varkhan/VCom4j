package net.varkhan.base.management.logging;

/**
 * <b>A logger resolver.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 11:05:34 PM
 *
 * @param <T> the log event type
 */
public interface LogResolver<T> extends LogConfig {

    /**
     * Get a log handler, for a given context and filter key.
     *
     * @param ctx a context name
     * @param key a filter key
     *
     * @return a log handler for those parameters
     */
    public Log<T> getLogger(String ctx, String key);

    /**
     * Dispatch a log event, with a event weight of {@literal 1}.
     *
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param msg the event content
     */
    public void log(String ctx, String key, int lev, T msg);

    /**
     * Dispatch a log event.
     *
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param val the event weight
     * @param msg the event content
     */
    public void log(String ctx, String key, int lev, double val, T msg);

    /**
     * Return the logging level mask.
     *
     * @param ctx a context name
     * @param key a filter key
     *
     * @return the enabled severity level mask for context {@code ctx} and key {@code key},
     *         with a non-zero sign bit (set bit of {@link LogConfig#SET_MARKER})
     *         or {@literal 0L} if the mask is not defined
     */
    public long getLevelMask(String ctx, String key);

    /**
     * Indicate whether a particular logging level is enabled.
     *
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     *
     * @return {@code true} iff logging for the severity level {@code lev} is enabled, for context {@code ctx} and key {@code key}
     */
    public boolean isLevelEnabled(String ctx, String key, int lev);

}
