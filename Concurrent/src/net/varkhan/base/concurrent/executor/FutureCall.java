package net.varkhan.base.concurrent.executor;

import net.varkhan.base.concurrent.Call;
import net.varkhan.base.concurrent.Future;

import java.util.concurrent.atomic.AtomicReference;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/16/11
 * @time 5:49 AM
 */
public abstract class FutureCall<V> extends FutureTask implements Call<V>, Future {
    private final AtomicReference<V> value = new AtomicReference<V>(null);
    private final AtomicReference<Exception> error = new AtomicReference<Exception>(null);

    public final V get() throws Exception {
        Exception e = error.get();
        if(e!=null) throw e;
        return value.get();
    }

    public final V get(long timeout) throws Exception {
        super.waitDone(timeout);
        return get();
    }

    protected final void success(V v) {
        value.set(v);
        super.complete();
    }

    protected final void failure(Exception e) {
        error.set(e);
        super.complete();
    }

}
