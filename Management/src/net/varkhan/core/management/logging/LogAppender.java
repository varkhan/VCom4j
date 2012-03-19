package net.varkhan.core.management.logging;

import java.io.Flushable;


public class LogAppender<T> implements LogWriter<T> {

    private final Appendable out;
    private final String     fmt;

    public LogAppender(Appendable out) {
        this(out, null);
    }

    public LogAppender(Appendable out, String fmt) {
        this.out=out;
        this.fmt=fmt;
    }

    public void log(String ctx, String key, int lev, long tms, T msg) {
        log(ctx, key, lev, tms, 1.0, msg);
    }

    public void log(String ctx, String key, int lev, long tms, double val, T msg) {
        try {
            if(fmt==null) LogFormat.formatAsText(out, ctx, key, lev, tms, val, msg);
            else LogFormat.format(out, fmt, ctx, key, lev, tms, val, msg);
        }
        catch(Throwable t) { /* ignore failing dispatches */ }
    }

    public void log(LogEvent<T> evt) {
        log(evt.getContext(),evt.getKey(),evt.getLevel(),evt.getTimeStamp(),evt.getWeight(),evt.getContent());
    }

    public void log(Iterable<LogEvent<T>> evts) {
        for(LogEvent<T> evt: evts) log(evt);
    }

    public void flush() {
        if(out instanceof Flushable) try { ((Flushable)out).flush(); }
        catch(Throwable t) { /* ignore failing dispatches */ }
    }
}
