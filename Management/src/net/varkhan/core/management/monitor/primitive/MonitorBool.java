package net.varkhan.core.management.monitor.primitive;

import net.varkhan.core.management.monitor.Monitor;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/29/10
 * @time 12:43 AM
 */
public interface MonitorBool extends Monitor<Boolean> {

    /**
     * Return the monitor value type.
     *
     * @return the type of the monitor's value
     */
    @Name("type")
    @Description("The value type of the monitor")
    public Class<Boolean> type();

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
    public Boolean value();

    /**
     * Update internal memory, updating value(s) to reflect the monitored process.
     */
    @Name("update")
    @Description("Updates internal memory, updating value(s) to reflect the monitored process")
    public void update();

}
