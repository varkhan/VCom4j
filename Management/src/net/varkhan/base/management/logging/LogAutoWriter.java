package net.varkhan.base.management.logging;

import java.util.ArrayList;
import java.util.List;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/28/11
 * @time 1:09 AM
 */
public class LogAutoWriter<T> extends LogBatchWriter<T> {

    private static int threadNumber;
    private static synchronized int nextThreadNumber() {
        return threadNumber++;
    }

    private volatile Thread thread = null;
    private final int batch;
    private final long period;

    public LogAutoWriter(LogWriter<T> wrt, int batch, long period) {
        super(wrt);
        this.batch=batch;
        this.period=period;
    }

    public void start() {
        if(thread!=null) return;
        thread = new Dispatcher();
        thread.setDaemon(true);
        thread.setName(LogAutoWriter.class.getSimpleName()+"-"+nextThreadNumber());
        thread.start();
    }

    public void stop() { stop(period); }

    public void stop(long timeout) {
        Thread t = thread;
        if(t==null) return;
        thread = null;
        try { t.join(timeout); }
        catch(InterruptedException e) { /* ignore */ }
    }

    private final class Dispatcher extends Thread {
        public void run() {
            final List<LogEvent<T>> buffer = new ArrayList<LogEvent<T>>(batch);
            while(thread!=null) {
                long time = System.currentTimeMillis();
                // Empty queue, one batch at a time
                while(!queue.isEmpty()) try {
                    drain(buffer, batch);
                    wrt.log(buffer);
                    buffer.clear();
                }
                catch(Throwable t) { /* ignore failed dispatches */ }
                long elapsed = System.currentTimeMillis()-time;
                if(elapsed<period) try { Thread.sleep(period-elapsed); }
                catch(InterruptedException e) { /* ignore */ }
            }
        }
    }

}
