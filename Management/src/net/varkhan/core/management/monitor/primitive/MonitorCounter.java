/**
 *
 */
package net.varkhan.core.management.monitor.primitive;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 3:11:52 AM
 */
public class MonitorCounter implements MonitorLong {

    public Class<Long> type() { return Long.class; }

    private volatile long c=0;

    public synchronized void reset() { c=0; }

    public synchronized void update() { c++; }

    public Long value() { return c; }

    public String toString() { return Long.toString(value()); }
}
