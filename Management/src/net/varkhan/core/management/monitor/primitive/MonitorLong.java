/**
 *
 */
package net.varkhan.core.management.monitor.primitive;

import net.varkhan.core.management.monitor.Monitor;


/**
 * <b>A monitored integer value.</b>
 * <p/>
 * A integer value that reflects the state of a monitored process or resource.
 * <p/>
 *
 * @author varkhan
 * @date Jun 16, 2009
 * @time 10:12:47 PM
 */
public interface MonitorLong extends Monitor<Long> {

    /**
     * Return the monitor value type.
     *
     * @return the type of the monitor's value
     */
    @Name("type")
    @Description("The value type of the monitor")
    public Class<Long> type(); // { return Long.class; }

    /**
     * Clear internal memory, and resets the value to initialization state.
     */
    @Name("reset")
    @Description("Clears internal memory, and resets the value to initialization state")
    public abstract void reset();

    /**
     * Return the current monitor value.
     *
     * @return the current internal value of the monitored state
     */
    @Name("value")
    @Description("The current monitor value")
    public abstract Long value();

    /**
     * Update internal memory, updating value(s) to reflect the monitored process.
     */
    @Name("update")
    @Description("Updates internal memory, updating value(s) to reflect the monitored process")
    public abstract void update();

}
