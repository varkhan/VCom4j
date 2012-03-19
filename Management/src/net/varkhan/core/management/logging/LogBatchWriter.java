package net.varkhan.core.management.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/27/11
 * @time 11:56 PM
 */
public class LogBatchWriter<T> implements LogWriter<T> {

    protected final AtomicInteger                      size = new AtomicInteger();
    protected final ConcurrentLinkedQueue<LogEvent<T>> queue = new ConcurrentLinkedQueue<LogEvent<T>>();
    protected final LogWriter<T>                       wrt;

    public LogBatchWriter(LogWriter<T> wrt) {
        this.wrt=wrt;
    }

    public void log(String ctx, String key, int lev, long tms, T msg) {
        log(new SimpleLogEvent<T>(ctx, key, lev, tms, 1.0, msg));
    }

    public void log(String ctx, String key, int lev, long tms, double val, T msg) {
        log(new SimpleLogEvent<T>(ctx, key, lev, tms, val, msg));
    }

    public void log(LogEvent<T> evt) {
        size.incrementAndGet();
        queue.add(evt);
    }

    public void log(Iterable<LogEvent<T>> evts) {
        for(LogEvent<T> evt: evts) log(evt);
    }

    public void flush() {
        int size=this.size.get();
        if(size<=0) size=1;
        List<LogEvent<T>> buffer = new ArrayList<LogEvent<T>>(size);
        drain(buffer);
        try { wrt.log(buffer); }
        catch(Throwable t) { /* ignore failed dispatches */ }
    }

    protected void drain(Collection<LogEvent<T>> evts) {
        LogEvent<T> evt;
        while((evt=queue.poll())!=null) {
            size.decrementAndGet();
            evts.add(evt);
        }
    }

    protected void drain(Collection<LogEvent<T>> evts, int count) {
        LogEvent<T> evt;
        while(count>0 && (evt=queue.poll())!=null) {
            size.decrementAndGet();
            evts.add(evt);
            count --;
        }
    }

    protected static class SimpleLogEvent<T> implements LogEvent<T> {
        private final String ctx;
        private final String key;
        private final int lev;
        private final long tms;
        private final double val;
        private final T msg;

        public SimpleLogEvent(String ctx, String key, int lev, long tms, double val, T msg) {
            this.ctx=ctx;
            this.key=key;
            this.lev=lev;
            this.tms=tms;
            this.val=val;
            this.msg=msg;
        }

        public String getContext() { return ctx; }
        public String getKey() { return key; }
        public int getLevel() { return lev; }
        public double getWeight() { return val; }
        public long getTimeStamp() { return tms; }
        public T getContent() { return msg; }
    }

}
