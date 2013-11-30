/**
 *
 */
package net.varkhan.base.management.monitor.primitive;

import net.varkhan.base.management.metric.Time;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 3:11:52 AM
 */
public class MonitorTime implements MonitorLong {

    private final Time time;

    public MonitorTime(Time time) {
        this.time=time;
        reset();
    }

    public Class<Long> type() { return Long.class; }

    private volatile long t=0;

    public synchronized void reset() { t=time.time(); }

    public void update() { }

    public Long value() { return time.time()-t; }

    public String toString() { return Long.toString(value()); }

}
