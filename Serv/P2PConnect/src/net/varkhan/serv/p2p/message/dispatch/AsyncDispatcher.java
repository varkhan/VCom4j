package net.varkhan.serv.p2p.message.dispatch;

import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.message.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 11:01 AM
 */
public class AsyncDispatcher implements MesgDispatcher {

    private final AtomicLong                       nextSequence=new AtomicLong();
    private final ConcurrentMap<Long,MesgReceiver> registry    =new ConcurrentHashMap<Long,MesgReceiver>();
    private final ConcurrentMap<String,MesgAction> symbols     =new ConcurrentHashMap<String,MesgAction>();
    private final Executor                executor;
    private final JMXAverageMonitorReport stats;
    private       Thread                  thread;
    private       long                    period;

    public AsyncDispatcher(Executor executor, JMXAverageMonitorReport stats, long period) {
        this.executor=executor;
        this.stats=stats;
        this.period=period;
        start();
    }


    public void addMethod(String key, MesgAction value) {
        symbols.put(key, value);
    }

    public MesgAction getMethod(String key) {
        return symbols.get(key);
    }

    public boolean hasMethod(String key) {
        return symbols.containsKey(key);
    }

    public void delMethod(String key) {
        symbols.remove(key);
    }

    public Set<String> methods() {
        return symbols.keySet();
    }

    public long register(MesgReceiver handler) {
        long sequence = nextSequence.incrementAndGet();
        registry.put(sequence, handler);
        return sequence;
    }

    public void unregister(long sequence) {
        MesgReceiver receiver = registry.remove(sequence);
        if(receiver!=null) receiver.release();
    }

    public void call(PeerAddress src, PeerAddress dst, String method, MesgPayload call, MesgPayload repl, long sequence, MesgSender send) {
        MesgAction m = symbols.get(method);
        if(m==null) return;
        executor.execute(new TaskRunner(src,dst,method,m,call,repl,sequence,send));
    }

    public void repl(PeerAddress src, PeerAddress dst, String method, MesgPayload repl, long sequence) {
        MesgReceiver receiver = registry.get(sequence);
        if(receiver==null) return;
        receiver.receive(src, dst, method, repl);
        if(receiver.finished()) {
            // Ensure we don't have double releases
            receiver = registry.remove(sequence);
            if(receiver!=null) receiver.release();
        }
    }

    private static final AtomicLong threadNumber=new AtomicLong();

    public void start() {
        if(thread!=null) return;
        thread = new TimeoutCleaner();
        thread.setDaemon(true);
        thread.setName(AsyncDispatcher.class.getSimpleName()+"-"+threadNumber.incrementAndGet());
        thread.start();
    }

    public void stop() { stop(period); }

    public void stop(long timeout) {
        Thread t = thread;
        if(t==null) return;
        thread = null;
        t.interrupt();
        try { t.join(timeout); }
        catch(InterruptedException e) { /* ignore */ }
    }

    private final class TimeoutCleaner extends Thread {
        public void run() {
            while(thread!=null) {
                long time = System.currentTimeMillis();
                // Clean up finished receivers
                for(Iterator<MesgReceiver> iterator=registry.values().iterator();iterator.hasNext();) {
                    MesgReceiver r=iterator.next();
                    try {
                        if(r.finished()) iterator.remove();
                    }
                    catch(Throwable t) {
                        // Failing receivers are forcibly removed
                        iterator.remove();
                    }
                }
                long elapsed = System.currentTimeMillis()-time;
                if(elapsed<period) try { Thread.sleep(period-elapsed); }
                catch(InterruptedException e) { /* ignore */ }
            }
        }
    }

    private final class TaskRunner implements Runnable {

        private PeerAddress src;
        private PeerAddress dst;
        private String      name;
        private MesgAction  impl;
        private MesgPayload call;
        private MesgPayload repl;
        private long        sequence;
        private MesgSender  send;

        public TaskRunner(PeerAddress src, PeerAddress dst, String name, MesgAction impl, MesgPayload call, MesgPayload repl, long sequence, MesgSender send) {
            this.src=src;
            this.dst=dst;
            //To change body of created methods use File | Settings | File Templates.
            this.name=name;
            this.impl=impl;
            this.call=call;
            this.repl=repl;
            this.sequence=sequence;
            this.send=send;
        }

        public void run() {
            try {
                impl.invoke(src, dst, name, call, repl);
            }
            finally {
                try {
                    send.send(src, dst, name, repl, sequence);
                }
                catch(Throwable t) {
                    stats.inc("Dispatcher.Call.error["+t.getClass().getSimpleName()+"]");
                }
            }
        }
    }

}
