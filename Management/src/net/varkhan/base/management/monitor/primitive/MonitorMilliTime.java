/**
 *
 */
package net.varkhan.base.management.monitor.primitive;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 3:11:52 AM
 */
public class MonitorMilliTime implements MonitorLong {

    public Class<Long> type() { return Long.class; }

    private volatile long t=System.currentTimeMillis();

    public synchronized void reset() { t=System.currentTimeMillis(); }

    public void update() { }

    public Long value() { return System.currentTimeMillis()-t; }

    public String toString() { return Long.toString(value()); }

}
