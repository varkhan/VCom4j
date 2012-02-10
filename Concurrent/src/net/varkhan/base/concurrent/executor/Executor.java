package net.varkhan.base.concurrent.executor;


import net.varkhan.base.concurrent.Task;
import net.varkhan.base.containers.Container;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/9/11
 * @time 7:42 PM
 */
public interface Executor<T extends Task> {

    public void execute(T task);

    public void execute(Container<T> tasks);

}
