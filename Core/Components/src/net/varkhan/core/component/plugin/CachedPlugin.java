package net.varkhan.core.component.plugin;

import java.util.concurrent.atomic.AtomicReference;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/18/14
 * @time 3:52 PM
 */
public abstract class CachedPlugin<T> implements Plugin<T> {
    protected final Class<T> target;
    protected final AtomicReference<Update<T>> ref=new AtomicReference<Update<T>>(new Update<T>(null, null, 0));

    public CachedPlugin(Class<T> target) { this.target=target; }

    @Override
    public Class<T> target() { return target; }

    @Override
    public T value() { return ref.get().instance; }

    @Override
    public Throwable failure() { return ref.get().failure; }

    @Override
    public boolean isUpdated() {
        // Compare load timestamp to source modification time
        Update<T> up=ref.get();
        return up!=null && up.timestamp>=modified();
    }

    @Override
    public boolean isAvailable() {
        final Update<T> pl = ref.get();
        return pl.failure==null && pl.instance!=null;
    }

    @Override
    public void update() {
        if(isUpdated()) return;
        Update<T> up;
        try { up=fetch(); }
        catch (Exception e) { up=new Update<T>(null, e, modified()); }
        ref.set(up);
    }

    @Override
    public void updateIfAvailable() {
        // Compare load timestamp to source modification time
        final Update<T> ex = ref.get();
        long ts=modified();
        if(ex.timestamp>=ts) return;
        Update<T> up;
        try { up=fetch(); }
        catch (Exception e) { up=new Update<T>(null, e, ts); }
        if(ex.instance==null||up.failure==null) ref.compareAndSet(ex,up);
    }

    protected abstract long modified();

    protected abstract Update<T> fetch() throws Exception;

    protected static class Update<I> {
        public final long timestamp;
        public final I instance;
        public final Throwable failure;

        protected Update(I instance, Throwable failure, long timestamp) {
            this.timestamp =timestamp;
            this.instance = instance;
            this.failure = failure;
        }
    }
}
