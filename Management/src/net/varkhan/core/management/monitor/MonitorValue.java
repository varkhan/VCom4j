/**
 *
 */
package net.varkhan.core.management.monitor;


/**
 * <b>A generic monitored value.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jun 25, 2009
 * @time 2:04:10 AM
 */
public abstract class MonitorValue<V> implements Monitor<V> {

    protected final    Class<V> t;    // The value type
    protected final    V        d;        // The default value
    protected volatile V        v;        // The current value

    /**
     * Creates an abstract monitor on a value
     *
     * @param v the initial monitored value
     */
    public MonitorValue(Class<V> t, V v) {
        this.t=t;
        this.d=v;
        this.v=v;
    }

    public Class<V> type() { return t; }

    public void reset() { this.v=this.d; }

    public void update() { }

    public V value() { return this.v; }

}
