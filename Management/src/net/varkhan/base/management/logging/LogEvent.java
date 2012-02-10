package net.varkhan.base.management.logging;

/**
 * <b>A log event container.</b>
 * <p/>
 *
 * @author varkhan
 * @date 5/27/11
 * @time 11:41 PM
 *
 * @param <T> the event content type
 */
public interface LogEvent<T> {

    /**
     * Get the event context name.
     *
     * @return the context name
     */
    public String getContext();

    /**
     * Get the event filter key.
     *
     * @return the filter key
     */
    public String getKey();

    /**
     * Get the event severity level.
     *
     * @return the severity level
     */
    public int getLevel();

    /**
     * Get the event weight.
     *
     * @return the event weight
     */
    public double getWeight();

    /**
     * Get the event time-stamp.
     *
     * @return the event time-stamp (in milliseconds)
     */
    public long getTimeStamp();

    /**
     * Get the event content (message).
     *
     * @return the event content
     */
    public T getContent();

}
