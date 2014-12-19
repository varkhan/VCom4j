package net.varkhan.core.component.module;

import net.varkhan.base.management.config.Configuration;
import net.varkhan.base.management.config.ConfigurationError;
import net.varkhan.base.management.state.lifecycle.LifeLevel;
import net.varkhan.base.management.state.lifecycle.LifeState;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 8:27 PM
 */
public final class DependencyManager {

    private final ConcurrentMap<Class,Module> instances = new ConcurrentHashMap<Class,Module>();

    private DependencyManager() { }

    public <C extends Module> void validate(Class<C> klass, Configuration conf) throws ConfigurationError {
        C inst=getInstance(klass);
        inst.validate(conf);
        for(Class<Module> k:inst.dependencies()) {
            validate(k,conf);
        }
    }

    public <C extends Module> void configure(Class<C> klass, Configuration conf) throws ConfigurationError {
        C inst=getInstance(klass);
        inst.configure(conf);
        for(Class<Module> k:inst.dependencies()) {
            configure(k,conf);
        }
    }

    public <C extends Module> void startup(Class<C> klass, String reason) {
        C inst=getInstance(klass);
        for(Class<Module> k:inst.dependencies()) {
            startup(k,reason);
        }
        inst.startup(reason);
    }

    public <C extends Module> void shutdown(Class<C> klass, String reason) {
        C inst=getInstance(klass);
        for(Class<Module> k:inst.dependencies()) {
            startup(k,reason);
        }
        inst.shutdown(reason);
    }

    public <C extends Module> String name(Class<C> klass) {
        return getInstance(klass).name();
    }

    public <C extends Module> String desc(Class<C> klass) {
        return getInstance(klass).desc();
    }

    public <C extends Module> LifeLevel level(Class<C> klass) {
        return getInstance(klass).level();
    }

    public <C extends Module> Collection<Class<Module>> dependencies(Class<C> klass) {
        return getInstance(klass).dependencies();
    }

    public <C extends Module> LifeState state(Class<C> klass) {
        return getInstance(klass).state();
    }

    public <C extends Module> String reason(Class<C> klass) {
        return getInstance(klass).reason();
    }



    @SuppressWarnings({ "unchecked" })
    public <C extends Module> C getInstance(Class<C> klass) throws IllegalArgumentException {
        if(instances.containsKey(klass)) return (C) instances.get(klass);
        try {
            C inst = klass.getConstructor().newInstance();
            if(instances.putIfAbsent(klass,inst)==null) return inst;
            return (C) instances.get(klass);
        }
        catch(InstantiationException e) {
            throw new IllegalArgumentException("Invalid component: abstract declaration",e);
        }
        catch(IllegalAccessException e) {
            throw new IllegalArgumentException("Invalid component: protected constructor",e);
        }
        catch(InvocationTargetException e) {
            throw new IllegalArgumentException("Illegal component constructor operation",e);
        }
        catch(NoSuchMethodException e) {
            throw new IllegalArgumentException("Invalid component: no no-arg constructor",e);
        }
    }

}
