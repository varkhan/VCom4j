package net.varkhan.core.component.module;

import net.varkhan.base.management.config.Configuration;
import net.varkhan.base.management.config.ConfigurationError;
import net.varkhan.base.management.state.lifecycle.LifeLevel;
import net.varkhan.base.management.state.lifecycle.LifeState;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 8:04 PM
 */
abstract class AbstractModule implements Module {
    protected final LifeLevel level;
    protected final String name;
    protected final String desc;
    protected volatile LifeState state = null;
    protected volatile String reason = null;

    AbstractModule(LifeLevel level, String name, String desc) {
        this.level=level;
        this.name=name;
        this.desc=desc;
    }

    @Override
    public String name() { return name; }

    @Override
    public String desc() { return desc; }

    @Override
    public LifeLevel level() { return level; }

    @Override
    public LifeState state() {
        return state;
    }

    @Override
    public String reason() {
        return reason;
    }

    @Override
    public void update() {
        // Nothing to do here
    }

    @Override
    public abstract void validate(Configuration conf) throws ConfigurationError;
    protected abstract void doConfigure() throws ConfigurationError;
    protected abstract void doStartup();
    protected abstract void doShutdown();


    @Override
    public final void configure(Configuration conf) throws ConfigurationError {
        if(state!=null) return;
        doConfigure();
        state=LifeState.STOPPED;
    }

    @Override
    public final void startup(String reason) {
        state=LifeState.STARTING;
        doStartup();
        state=LifeState.RUNNING;
    }

    @Override
    public final void shutdown(String reason) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isConfigured() {
        return state!=null;
    }

    @Override
    public boolean isStarted() {
        return state==LifeState.RUNNING;
    }

    @Override
    public boolean isStopped() {
        return state==LifeState.STOPPED;
    }
}
