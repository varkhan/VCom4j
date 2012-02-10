/**
 *
 */
package net.varkhan.base.management.monitor.primitive;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 3:11:52 AM
 */
public class MonitorNanoTime implements MonitorLong {

    public Class<Long> type() { return Long.class; }

    private volatile long t=System.nanoTime();

    public synchronized void reset() { t=System.nanoTime(); }

    public void update() { }

    public Long value() { return System.nanoTime()-t; }

    public String toString() { return Long.toString(value()); }

}
