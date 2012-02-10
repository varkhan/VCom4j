package net.varkhan.base.concurrent.executor;

import net.varkhan.base.concurrent.Future;
import net.varkhan.base.concurrent.Task;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/16/11
 * @time 5:49 AM
 */
public abstract class FutureTask implements Future {
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicBoolean completed= new AtomicBoolean(false);

    public boolean cancel() {
        cancelled.set(true);
        this.notify();
        return true;
    }

    public boolean isCancelled() {
        return cancelled.get();
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public boolean isDone() {
        return cancelled.get() || completed.get();
    }

    public boolean waitDone(long timeout) {
        // If to>0 wait for specified timeout
        if(timeout>0) {
            long time = System.currentTimeMillis();
            // Loop until either completed or cancelled
            while(!(completed.get()||cancelled.get())) {
                long curr = System.currentTimeMillis();
                // Wait for remaining time, if any, or until notified of an update
                if(time+timeout<=curr) break;
                try { this.wait(time+timeout-curr); }
                catch(InterruptedException e) { break; /* stop waiting if interrupted */ }
            }
        }
        // If to<0 wait forever
        else if(timeout<0) {
            while(!(completed.get()||cancelled.get())) {
                // Wait until notified of an update
                try { this.wait(); }
                catch(InterruptedException e) { break; /* stop waiting if interrupted */ }
            }
        }
        // If to==0 do not wait
        return completed.get();
    }

    protected void complete() {
        completed.set(true);
        this.notify();
    }


}
