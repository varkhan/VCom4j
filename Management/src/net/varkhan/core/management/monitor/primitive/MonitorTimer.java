/**
 *
 */
package net.varkhan.core.management.monitor.primitive;

import net.varkhan.core.management.metric.Timer;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 3:11:52 AM
 */
public class MonitorTimer implements MonitorLong {

    private final Timer timer;

    public MonitorTimer(Timer timer) { this.timer=timer; }

    public Class<Long> type() { return Long.class; }

    public synchronized void reset() { timer.reset(); }

    public void update() { }

    public Long value() { return timer.time(); }

    public String toString() { return Long.toString(value()); }

}
