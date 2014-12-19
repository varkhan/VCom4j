package net.varkhan.core.component.module;

import net.varkhan.base.management.config.Configuration;
import net.varkhan.base.management.config.ConfigurationError;
import net.varkhan.base.management.state.StateCheck;
import net.varkhan.base.management.state.lifecycle.LifeLevel;
import net.varkhan.base.management.state.lifecycle.LifeState;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 7:48 PM
 */
public interface Module extends StateCheck<LifeLevel,LifeState> {

    public String name();

    public String desc();

    public Collection<Class<Module>> dependencies();

    public void validate(Configuration conf) throws ConfigurationError;

    public LifeLevel level();

    public void configure(Configuration conf) throws ConfigurationError;

    public void startup(String reason);

    public void shutdown(String reason);

    public LifeState state();

    public String reason();

    public void update();

    public boolean isConfigured();

    public boolean isStarted();

    public boolean isStopped();


}
