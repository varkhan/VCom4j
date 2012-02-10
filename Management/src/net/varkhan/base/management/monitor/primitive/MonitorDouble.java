/**
 *
 */
package net.varkhan.base.management.monitor.primitive;

import net.varkhan.base.management.monitor.Monitor;


/**
 * <b>A monitored numeric value.</b>
 * <p/>
 * A numeric value that reflects the state of a monitored process or resource.
 * <p/>
 *
 * @author varkhan
 * @date Jun 16, 2009
 * @time 10:12:47 PM
 */
public interface MonitorDouble extends Monitor<Double> {

    /**
     * Return the monitor value type.
     *
     * @return the type of the monitor's value
     */
    @Name("type")
    @Description("The value type of the monitor")
    public Class<Double> type(); // { return Double.class; }

    /**
     * Clear internal memory, and resets the value to initialization state.
     */
    @Name("reset")
    @Description("Clears internal memory, and resets the value to initialization state")
    public void reset();

    /**
     * Return the current monitor value.
     *
     * @return the current internal value of the monitored state
     */
    @Name("value")
    @Description("The current monitor value")
    public Double value();

    /**
     * Update internal memory, updating value(s) to reflect the monitored process.
     */
    @Name("update")
    @Description("Updates internal memory, updating value(s) to reflect the monitored process")
    public void update();

}
