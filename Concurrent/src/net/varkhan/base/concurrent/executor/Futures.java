package net.varkhan.base.concurrent.executor;

import net.varkhan.base.concurrent.Call;
import net.varkhan.base.concurrent.Task;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/17/11
 * @time 8:36 PM
 */
public final class Futures {


    protected Futures() { }

    public static FutureTask execute(Executor<Task> exec, Task task) {
        FTask f = new FTask(task);
        exec.execute(f);
        return f;
    }

    public static <V> FutureCall<V> execute(Executor<Task> exec, Call<V> call) {
        FCall<V> f = new FCall<V>(call);
        exec.execute(f);
        return f;
    }

    private static class FTask extends FutureTask implements Task {
        private final Task task;
        public FTask(Task task) { this.task=task; }
        public void invoke() { task.invoke(); }
    }

    private static class FCall<V> extends FutureCall<V> implements Task {
        private final Call<V> call;
        public FCall(Call<V> call) { this.call=call; }
        public void invoke() {
            try { success(call.get()); }
            catch(Exception e) { failure(e); }
        }
    }

}
