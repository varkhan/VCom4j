package net.varkhan.serv;

import net.varkhan.base.management.monitor.Monitor;
import net.varkhan.base.management.report.JMXMonitorReport;
import net.varkhan.base.management.report.MonitorReport;
import net.varkhan.base.management.state.ConcurrentStateReport;
import net.varkhan.base.management.state.JMXStateReport;
import net.varkhan.base.management.state.StateReport;
import net.varkhan.base.management.state.health.HealthLevel;
import net.varkhan.base.management.state.health.HealthState;
import net.varkhan.base.management.state.lifecycle.LifeLevel;
import net.varkhan.base.management.state.lifecycle.LifeState;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 8:07 PM
 */
public abstract class BaseServer implements Monitored {
    protected volatile boolean configured = false;
    protected final ConcurrentStateReport<HealthLevel,HealthState> health = new ConcurrentStateReport<HealthLevel,HealthState>(HealthState.HEALTHY, 100);
    protected final ConcurrentStateReport<LifeLevel,LifeState> status = new ConcurrentStateReport<LifeLevel,LifeState>(LifeState.RUNNING, 100);
    protected final MonitorReport<Monitor<?>> monitors = new JMXMonitorReport<Monitor<?>>();

    @Override
    public StateReport<HealthLevel,HealthState> health() {
        return health;
    }

    @Override
    public StateReport<LifeLevel,LifeState> status() {
        return status;
    }

    @Override
    public MonitorReport<Monitor<?>> monitors() {
        return monitors;
    }

    public void configure() throws Exception {
        if(configured) return;
        JMXStateReport<HealthLevel,HealthState> jmxh = new JMXStateReport<HealthLevel,HealthState>(health, "health");
        jmxh.register();
        JMXStateReport<LifeLevel,LifeState> jmxs = new JMXStateReport<LifeLevel,LifeState>(status, "status");
        jmxs.register();
        configured = true;
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    public abstract void join() throws Exception;

}
