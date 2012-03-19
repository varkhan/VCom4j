/**
 *
 */
package net.varkhan.core.management.monitor.primitive;


import net.varkhan.core.management.monitor.MonitorWritable;


/**
 * <b>A monitored flag.</b>
 * <p/>
 * A boolean flag that reflects the state of a monitored process or resource.
 * <p/>
 *
 * @author varkhan
 * @date Jun 16, 2009
 * @time 10:12:47 PM
 */
public abstract class MonitorBoolWritable extends MonitorWritable<Boolean> implements MonitorBool {

    /**
     * Creates a new writable boolean monitor, with the specified initial value.
     */
    public MonitorBoolWritable(boolean v) {
        super(Boolean.class, v);
    }

    /**
     * Creates a new writable boolean monitor.
     */
    public MonitorBoolWritable() {
        super(Boolean.class, false);
    }

    /**
     * Sets the value of this monitor, and updates the set count
     *
     * @param v the new value
     */
    public void set(boolean v) {
        this.v=v;
        this.c++;
    }

}
