package net.varkhan.base.management.logging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * <b>A log writer that dispatches log events according to key prefix.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 11:05:34 PM
 *
 * @param <T> the event type
 */
public class LogDispatchPrefix<T> implements LogWriter<T> {

    private final ConcurrentMap<String,LogWriter<T>> dsp = new ConcurrentHashMap<String,LogWriter<T>>();

    public LogDispatchPrefix() { }

    public LogDispatchPrefix(Map<String,LogWriter<T>> dsp) {
        this.dsp.putAll(dsp);
    }

    public void log(String ctx, String key, int lev, long tms, T msg) {
        final LogWriter<T> wrt=get(key);
        if(wrt!=null) wrt.log(ctx, key, lev, tms, msg);
    }

    public void log(String ctx, String key, int lev, long tms, double val, T msg) {
        final LogWriter<T> wrt=get(key);
        if(wrt!=null) wrt.log(ctx, key, lev, tms, val, msg);
    }

    public void log(LogEvent<T> evt) {
        final LogWriter<T> wrt=get(evt.getKey());
        if(wrt!=null) wrt.log(evt);
    }

    public void log(Iterable<LogEvent<T>> evts) {
        for(LogEvent<T> evt: evts) log(evt);
    }

    public void flush() {
        for(LogWriter<T> wrt: dsp.values()) wrt.flush();
    }

    protected LogWriter<T> get(String key) {
        // Attempt to find exact key
        LogWriter<T> wrt=dsp.get(key);
        if(wrt!=null) return wrt;
        // Search for dot-delimited prefixes of the key
        int dot=key.lastIndexOf('.');
        while(dot>0) {
            dot--;
            String sk=key.substring(0, dot);
            wrt=dsp.get(sk);
            if(wrt!=null) return wrt;
            dot=key.lastIndexOf('.', dot);
        }
        // No more dots in key, look for empty key
        return dsp.get("");
    }

    public void set(String key, LogWriter<T> wrt) {
        this.dsp.put(key,wrt);
    }

    public void del(String key) {
        this.dsp.remove(key);
    }

}
