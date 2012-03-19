package net.varkhan.core.management.logging;

import net.varkhan.base.functors.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/21/12
 * @time 10:35 PM
 */
public class LogMapper<L> implements Logger<L> {

    private final Logger<Object> log;
    private final Mapper<Object,L,String> map;

    @SuppressWarnings({ "unchecked" })
    public <T> LogMapper(Logger<T> log, Mapper<T,L,String> map) {
        this.log = (Logger<Object>) log;
        this.map = (Mapper<Object, L, String>) map;
    }

    public String getContext() { return log.getContext(); }

    public String getKey() { return log.getKey(); }

    public long getLevelMask() { return log.getLevelMask(); }

    public boolean isLevelEnabled(int lev) { return log.isLevelEnabled(lev); }

    public void log(int lev, L msg) {
        log.log(lev, map.invoke(msg, log.getContext()));
    }

    public void log(int lev, double val, L msg) {
        log.log(lev, val, map.invoke(msg, log.getContext()));
    }
}
