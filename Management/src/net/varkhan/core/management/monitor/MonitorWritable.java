/**
 *
 */
package net.varkhan.core.management.monitor;

import net.varkhan.core.management.monitor.primitive.MonitorLong;


/**
 * <b>A generic writable monitored value.<b/>
 * <p/>
 * A value that can be written, and keeps track of the number of updates.
 * <p/>
 *
 * @author varkhan
 * @date Jun 25, 2009
 * @time 2:04:10 AM
 */
public class MonitorWritable<V> extends MonitorValue<V> implements MonitorAggregate<MonitorWritable.Count,V,Long> {

    protected volatile long c;

    /**
     * Creates a new writable monitor, with the specified initial value.
     */
    public MonitorWritable(Class<V> t, V v) {
        super(t, v);
        this.c=0;
    }

    private final MonitorLong cm=new MonitorLong() {
        public Class<Long> type() { return Long.class; }

        public void reset() { c=0; }

        public void update() { }

        public Long value() { return c; }
    };


    public enum Count {COUNT}


    ;

    public MonitorLong component(MonitorWritable.Count c) { return cm; }

    public Monitor<Long> component(String n) { return cm; }

    public MonitorWritable.Count[] components() { return Count.values(); }

    public Long value(MonitorWritable.Count c) { return this.c; }

    public Long value(String n) { return this.c; }

    /**
     * Returns a monitor for update count
     *
     * @return a monitor on the number of updates since the last count reset
     */
    public MonitorLong count() { return cm; }

    /**
     * Sets the value of this monitor, and updates the set count
     *
     * @param v the new value
     */
    public void set(V v) {
        this.v=v;
        this.c++;
    }


}
