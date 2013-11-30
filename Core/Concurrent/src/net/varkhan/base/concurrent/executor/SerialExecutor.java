package net.varkhan.base.concurrent.executor;

import net.varkhan.base.concurrent.Task;
import net.varkhan.base.containers.Container;


/**
 * <b>Serial executor</b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/9/11
 * @time 7:45 PM
 */
public class SerialExecutor<T extends Task> implements Executor<T> {

    public void execute(T task) {
        task.invoke();
    }

    public void execute(Container<T> tasks) {
        for(T task: (Iterable<T>) tasks) {
            task.invoke();
        }
    }

}
