/**
 *
 */
package net.varkhan.base.management.monitor.derived;

import net.varkhan.base.management.monitor.primitive.MonitorBoolWritable;
import net.varkhan.base.management.monitor.primitive.MonitorDouble;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 4:10:24 AM
 */
public class MonitorStateCooldown extends MonitorBoolWritable {

    private final MonitorDouble val;
    private       double        rel, trg;
    private volatile boolean state=false;

    public MonitorStateCooldown(MonitorDouble val, double rel, double trg) {
        this.val=val;
        this.rel=rel;
        this.trg=trg;
    }

    public double getRelease() { return this.rel; }

    public void setRelease(double rel) { this.rel=rel; }

    public double getTrigger() { return this.trg; }

    public void setTrigger(double trg) { this.trg=trg; }

    public void reset() { val.reset(); }

    public void update() { val.update(); }

    public Boolean value() {
        double v=val.value();
        if(v>trg) state=true;
        else if(v<rel) state=false;
        return state;
    }

    public String toString() { return value() ? "true" : "false"; }

}
