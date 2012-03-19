/**
 *
 */
package net.varkhan.core.management.monitor.derived;

import net.varkhan.core.management.monitor.primitive.MonitorBoolWritable;
import net.varkhan.core.management.monitor.primitive.MonitorDouble;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 4:10:24 AM
 */
public class MonitorStateMinMax extends MonitorBoolWritable {

    private final MonitorDouble val;
    private       double        min, max;

    public MonitorStateMinMax(MonitorDouble val, double min, double max) {
        this.val=val;
        this.min=min;
        this.max=max;
    }

    public double getMin() { return this.min; }

    public void setMin(double min) { this.min=min; }

    public double getMax() { return this.max; }

    public void setMax(double max) { this.max=max; }

    public void reset() { val.reset(); }

    public void update() { val.update(); }

    public Boolean value() {
        double v=val.value();
        return v>=min&&v<=max;
    }

    public String toString() { return value() ? "true" : "false"; }

}
