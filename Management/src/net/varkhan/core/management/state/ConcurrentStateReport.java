package net.varkhan.core.management.state;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:57 PM
 */
public class ConcurrentStateReport<L extends Level,S extends State<L,S>> implements StateReport<L,S> {
    protected final Collection<StateCheck<L,S>> checks = new CopyOnWriteArrayList<StateCheck<L,S>>();
    protected final S initial;
    protected final long period;
    protected volatile Updater updater = null;

    public ConcurrentStateReport(S initial, long period) { this.initial=initial; this.period=period; }

    public void add(StateCheck<L,S> hc) { checks.add(new ConcurrentStateCheck<L,S>(hc)); }

    public void start() {
        if(updater!=null && updater.isAlive()) return;
        synchronized(this) {
            updater = new Updater(period);
            updater.start();
        }
    }

    public void stop() {
        if(updater==null || !updater.isAlive()) return;
        synchronized(this) {
            updater.release();
        }
    }

    public S state() {
        S state = initial;
        for(StateCheck<L,S> hc: checks) {
            hc.update();
            S s = hc.state();
            if(s==null) continue;
            if(state==null) state = s;
            else state = state.aggregate(s, hc.level());
        }
        return state;
    }

    public Collection<StateCheck<L,S>> checks() { return Collections.unmodifiableCollection(checks); }

    public void update() { if(updater!=null) updater.update(); }

    protected class Updater extends Thread {
        protected volatile boolean run = true;
        protected final long period;

        public Updater(long period) {
            this.period=period;
            this.setName(StateReport.class.getSimpleName()+Updater.class.getSimpleName()+"-"+getId());
            this.setDaemon(true);
        }

        public void run() {
            while(run) {
                long time = System.currentTimeMillis();
                for(StateCheck<L,S> hc: checks) try { hc.update(); } catch(Throwable t) { /* ignore */ }
                long elapsed = System.currentTimeMillis()-time;
                if(elapsed<period) try { Thread.sleep(period-elapsed); }
                catch(InterruptedException e) { /* ignore */ }
            }
        }

        public void update() { this.interrupt(); }

        public void end() {
            this.run = false;
            this.interrupt();
        }

        public void release() {
            this.run = false;
            this.interrupt();
            try { this.join(period); }
            catch(InterruptedException e) { /* ignore */ }
        }
    }


    /**
     * <b></b>.
     * <p/>
     *
     * @author varkhan
     * @date 1/22/11
     * @time 10:43 PM
     */
    public static class ConcurrentStateCheck<L extends Level,S extends State<L,S>> extends WrapperStateCheck<L,S> implements StateCheck<L,S> {

        protected final AtomicReference<Update<S>> update = new AtomicReference<Update<S>>(null);

        public ConcurrentStateCheck(StateCheck<L,S> hc) { super(hc); }

        public S state() {
            Update<S> u = update.get();
            if(u==null) return null;
            return u.status();
        }

        public String reason() {
            Update<S> u = update.get();
            if(u==null) return null;
            return u.reason();
        }

        public void update() {
            Update<S> u;
            try { u = new Update<S>(hc.state(),hc.reason()); }
            catch(Throwable t) { u = new Update<S>(null,t.toString()); }
            update.set(u);
        }

        protected static class Update<S> {
            public final S s;
            public final String r;

            public Update(S s, String r) {
                this.s=s;
                this.r=r;
            }

            public S status() {
                return s;
            }

            public String reason() {
                return r;
            }
        }

    }
}
