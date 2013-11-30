package net.varkhan.base.management.logging;

import net.varkhan.base.management.metric.MilliTime;
import net.varkhan.base.management.metric.Time;


public class StaticLogger<T> implements Logger<T> {

    private final String       ctx;
    private final String       key;
    private final long         msk;
    private final LogWriter<T> wrt;
    private final Time         tmr;

    /**
     * Create a static log, with fixed logging level and a millisecond timer.
     *
     * @param ctx the logging context
     * @param key the logging key
     * @param lev the logging level
     * @param wrt a log writer
     */
    public StaticLogger(String ctx, String key, long lev, LogWriter<T> wrt) {
        this(ctx, key, lev, wrt, new MilliTime());
    }

    /**
     * Create a static log, with fixed logging level.
     *
     * @param ctx the logging context
     * @param key the logging key
     * @param lev the logging level
     * @param wrt a log writer
     * @param tmr a log timer
     */
    public StaticLogger(String ctx, String key, long lev, LogWriter<T> wrt, Time tmr) {
        this.ctx=ctx;
        this.key=key;
        this.msk=lev;
        this.wrt=wrt;
        this.tmr=tmr;
    }

    public String getContext() { return ctx; }

    public String getKey() { return key; }

    public long getLevelMask() { return msk; }

    public boolean isLevelEnabled(int lev) { return (msk&(1<<lev))!=0; }

    public void log(int lev, T msg) { wrt.log(ctx, key, lev, tmr.time(), msg); }

    public void log(int lev, double val, T msg) { wrt.log(ctx, key, lev, tmr.time(), val, msg); }

}
