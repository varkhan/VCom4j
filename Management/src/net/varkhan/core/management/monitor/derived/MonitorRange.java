/**
 *
 */
package net.varkhan.core.management.monitor.derived;

import net.varkhan.core.management.monitor.primitive.MonitorBoolWritable;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 4:10:24 AM
 */
public class MonitorRange<R extends MonitorBoolWritable,T extends MonitorBoolWritable> extends MonitorBoolWritable {

    private final boolean def;
    private final R       rel;
    private final T       trg;

    private volatile boolean state;

    public MonitorRange(R rel, T trg) {
        this(rel, trg, false);
    }

    public MonitorRange(R rel, T trg, boolean def) {
        this.rel=rel;
        this.trg=trg;
        this.def=def;
        this.state=def;
    }

    public R getRelease() { return this.rel; }

    public T getTrigger() { return this.trg; }

    public void reset() {
        trg.reset();
        rel.reset();
        state=def;
    }

    public void update() {
        trg.update();
        rel.update();
    }

    public Boolean value() {
        if(trg.value()) state=true;
        else if(rel.value()) state=false;
        return state;
    }

    public String toString() { return value() ? "true" : "false"; }

}
