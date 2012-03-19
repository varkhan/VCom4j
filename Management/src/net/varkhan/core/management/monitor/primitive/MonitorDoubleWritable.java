/**
 *
 */
package net.varkhan.core.management.monitor.primitive;

import net.varkhan.core.management.monitor.MonitorWritable;


/**
 * <b>A writable numeric monitored value.<b/>
 * <p/>
 * A numeric value that can be written, and keeps track of the number of updates.
 * <p/>
 *
 * @author varkhan
 * @date Jun 25, 2009
 * @time 2:04:10 AM
 */
public class MonitorDoubleWritable extends MonitorWritable<Double> implements MonitorDouble {

    /**
     * Creates a new writable numeric monitor, with the specified initial value.
     */
    public MonitorDoubleWritable(double v) {
        super(Double.class, v);
    }

    /**
     * Creates a new writable numeric monitor.
     */
    public MonitorDoubleWritable() {
        super(Double.class, 0.0);
    }

    /**
     * Sets the value of this monitor, and updates the set count
     *
     * @param v the new value
     */
    public void set(double v) {
        this.v=v;
        this.c++;
    }

    /**
     * Increments the value of this monitor, and updates the set count
     *
     * @param v the value increment
     */
    public void inc(double v) {
        this.v+=v;
        this.c++;
    }

    /**
     * Increments of 1.0 the value of this monitor, and updates the set count
     */
    public void inc() {
        this.v++;
        this.c++;
    }

}
