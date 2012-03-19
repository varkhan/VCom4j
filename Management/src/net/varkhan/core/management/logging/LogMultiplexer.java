package net.varkhan.core.management.logging;

import java.util.concurrent.atomic.AtomicReference;


public class LogMultiplexer<T> implements LogWriter<T> {

    private final AtomicReference<LogWriter[]> mtx = new AtomicReference<LogWriter[]>();

    public LogMultiplexer() { }

    public LogMultiplexer(LogWriter<T>... mtx) {
        this.mtx.set(mtx);
    }

    @SuppressWarnings( { "unchecked" })
    public void log(String ctx, String key, int lev, long tms, T msg) {
        final LogWriter[] mtx = this.mtx.get();
        if(mtx==null || mtx.length==0) return;
        for(LogWriter lw: mtx) try {
            lw.log(ctx, key, lev, tms, msg);
        }
        catch(Throwable t) { /* ignore failing dispatches */ }
    }

    @SuppressWarnings( { "unchecked" })
    public void log(String ctx, String key, int lev, long tms, double val, T msg) {
        final LogWriter[] mtx = this.mtx.get();
        if(mtx==null || mtx.length==0) return;
        for(LogWriter lw: mtx) try {
            lw.log(ctx, key, lev, tms, val, msg);
        }
        catch(Throwable t) { /* ignore failing dispatches */ }
    }

    @SuppressWarnings( { "unchecked" })
    public void log(LogEvent<T> evt) {
        final LogWriter[] mtx = this.mtx.get();
        if(mtx==null || mtx.length==0) return;
        for(LogWriter lw: mtx) try {
            lw.log(evt);
        }
        catch(Throwable t) { /* ignore failing dispatches */ }
    }

    @SuppressWarnings( { "unchecked" })
    public void log(Iterable<LogEvent<T>> evts) {
        final LogWriter[] mtx = this.mtx.get();
        if(mtx==null || mtx.length==0) return;
        for(LogWriter lw: mtx) try {
            lw.log(evts);
        }
        catch(Throwable t) { /* ignore failing dispatches */ }
    }

    public void flush() {
        final LogWriter[] mtx = this.mtx.get();
        if(mtx==null || mtx.length==0) return;
        for(LogWriter lw: mtx) try {
            lw.flush();
        }
        catch(Throwable t) { /* ignore failing dispatches */ }
    }

    public void set(LogWriter<T>... mtx) {
        this.mtx.set(mtx.clone());
    }

    public void add(LogWriter<T>... mtx) {
        LogWriter[] mtx_n;
        LogWriter[] mtx_o;
        do {
            mtx_o = this.mtx.get();
            if(mtx_o==null || mtx_o.length==0) {
                mtx_n = new LogWriter[mtx.length];
                System.arraycopy(mtx,0,mtx_n,0,mtx.length);
            }
            else {
                mtx_n = new LogWriter[mtx_o.length+mtx.length];
                System.arraycopy(mtx_o,0,mtx_n,0,mtx_o.length);
                System.arraycopy(mtx,0,mtx_n,mtx_o.length,mtx.length);
            }
        }
        while(!this.mtx.compareAndSet(mtx_o,mtx_n));
    }

}
