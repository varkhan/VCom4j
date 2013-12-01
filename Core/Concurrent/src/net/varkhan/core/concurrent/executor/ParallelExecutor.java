package net.varkhan.core.concurrent.executor;

import net.varkhan.core.concurrent.Task;
import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.array.Arrays;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;


/**
 * <b>Parallel executor</b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/9/11
 * @time 7:49 PM
 */
public class ParallelExecutor<T extends Task> implements Executor<T> {

    private final int         maxTasks;
    private final AtomicLong  numTasks = new AtomicLong();
    private final Queue<Task> runQueue = new ConcurrentLinkedQueue<Task>();
    private volatile long     timeout  = -1;
    private final ThreadGroup runGroup = new ThreadGroup(ParallelExecutor.class.getSimpleName());

    public ParallelExecutor(int maxTasks, long timeout) {
        this.maxTasks=maxTasks;
        this.timeout =timeout;
    }

    public ParallelExecutor(int maxTasks) {
        this(maxTasks,-1);
    }

    private void runtask(Task task) {
        runQueue.add(task);
        if(!runQueue.isEmpty() && numTasks.get()<maxTasks) {
            // Start a new thread to pick up tasks
            TaskThread t = new TaskThread();
            t.start();
        }
    }

    public void execute(T task) {
        execute(Arrays.asList(task));
    }

    public void execute(Container<T> tasks) {
        long time = System.currentTimeMillis();
        final AtomicLong remTasks = new AtomicLong(tasks.size());
        for(final T task: (Iterable<T>) tasks) {
            runtask(new Task() {
                public void invoke() {
                    try {
                        task.invoke();
                    }
                    finally {
                        remTasks.decrementAndGet();
                        remTasks.notify();
                    }
                }
            });
        }
        // Wait for result as long as the timeout allows
        if(timeout>0) while(remTasks.get()>0) {
            long curr = System.currentTimeMillis();
            if(time+timeout<=curr) break;
            try { remTasks.wait(time+timeout-curr); }
            catch(InterruptedException e) { break; /* stop waiting if interrupted */ }
        }
        // Wait forever
        else if(timeout<0) while(remTasks.get()>0) {
            try { remTasks.wait(); }
            catch(InterruptedException e) { break; /* stop waiting if interrupted */ }
        }
        // Do not wait if timeout==0
    }

    private class TaskThread extends Thread {
        public TaskThread() {
            super(runGroup, null, ParallelExecutor.class.getSimpleName()+numTasks.incrementAndGet());
            this.setDaemon(true);
        }

        public void run() {
            try {
                while(!runQueue.isEmpty()) {
                    Task task = runQueue.poll();
                    if(task==null) break;
                    // Notify runtask that we picked up some work
                    runQueue.notify();
                    task.invoke();
                }
                // Terminate thread when queue dries out
            }
            finally {
                numTasks.decrementAndGet();
                numTasks.notify();
            }
        }
    }

}
