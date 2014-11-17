package net.varkhan.serv;

import net.varkhan.base.management.monitor.Monitor;
import net.varkhan.base.management.report.MonitorReport;
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
 * @time 8:03 PM
 */
public interface Monitored {

    public StateReport<HealthLevel,HealthState> health();

    public StateReport<LifeLevel,LifeState> status();

    public MonitorReport<Monitor<?>> monitors();

}
