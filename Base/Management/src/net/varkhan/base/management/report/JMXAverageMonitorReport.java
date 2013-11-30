/**
 *
 */
package net.varkhan.base.management.report;

import net.varkhan.base.management.monitor.derived.MonitorContinuousAverage;
import net.varkhan.base.management.monitor.primitive.MonitorDouble;
import net.varkhan.base.management.monitor.primitive.MonitorDoubleWritable;


/**
 * <b>.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 12, 2009
 * @time 8:11:54 AM
 */
public class JMXAverageMonitorReport extends JMXMonitorReport<MonitorDouble> {

    /**
     * @param path
     */
    public JMXAverageMonitorReport(long halflife, String... path) {
        super(path);
        hl=halflife;
    }

    private long hl;

    public void add(String name, MonitorContinuousAverage m) {
        super.add(name, m);
    }

    public void del(String name) {
        super.del(name);
    }

    public MonitorDouble get(String name) {
        MonitorDouble m=super.get(name);
        if(m!=null) return m;
        MonitorDoubleWritable v=new MonitorDoubleWritable(0);
        MonitorContinuousAverage a=new MonitorContinuousAverage(v.count(), v, hl);
        super.add(name, v);
        super.add(name+".avg", a);
        return v;
    }

    public void inc(String name, double val) {
        MonitorDouble m=get(name);
        if(m instanceof MonitorDoubleWritable) {
            MonitorDoubleWritable v=(MonitorDoubleWritable) m;
            v.inc(val);
        }
    }

    public void inc(String name) {
        MonitorDouble m=get(name);
        if(m instanceof MonitorDoubleWritable) {
            MonitorDoubleWritable v=(MonitorDoubleWritable) m;
            v.inc(1.0);
        }
    }

}
