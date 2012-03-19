package net.varkhan.core.management.logging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 *<b>A log writer that dispatches log events according to key prefix.</b>
 * @param <T>
 */
public class LogDispatchContext<T> implements LogWriter<T> {

    private final LogWriter<T> def;
    private final ConcurrentMap<String,LogWriter<T>> dsp = new ConcurrentHashMap<String,LogWriter<T>>();

    public LogDispatchContext(LogWriter<T> def) {
        this.def = def;
    }

    public LogDispatchContext(LogWriter<T> def, Map<String,LogWriter<T>> dsp) {
        this.def = def;
        this.dsp.putAll(dsp);
    }

    public void log(String ctx, String key, int lev, long tms, T msg) {
        LogWriter<T> wrt=this.dsp.get(ctx);
        if(wrt==null) wrt=this.def;
        if(wrt!=null) wrt.log(ctx, key, lev, tms, msg);
    }

    public void log(String ctx, String key, int lev, long tms, double val, T msg) {
        LogWriter<T> wrt=this.dsp.get(ctx);
        if(wrt==null) wrt=this.def;
        if(wrt!=null) wrt.log(ctx, key, lev, tms, val, msg);
    }

    public void log(LogEvent<T> evt) {
        LogWriter<T> wrt=this.dsp.get(evt.getContext());
        if(wrt==null) wrt=this.def;
        if(wrt!=null) wrt.log(evt);
    }

    public void log(Iterable<LogEvent<T>> evts) {
        for(LogEvent<T> evt: evts) log(evt);
    }

    public void flush() {
        for(LogWriter<T> wrt: dsp.values()) wrt.flush();
    }

    public void set(String ctx, LogWriter<T> wrt) {
        this.dsp.put(ctx,wrt);
    }

    public void del(String ctx) {
        this.dsp.remove(ctx);
    }

}
