package net.varkhan.core.concurrent;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/17/11
 * @time 7:46 PM
 */
public interface Future {

    public boolean cancel();

    public boolean isCompleted();

    public boolean isCancelled();

    public boolean waitDone(long timeout);

}
