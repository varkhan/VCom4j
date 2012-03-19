package net.varkhan.core.management.state.lifecycle;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/31/11
 * @time 12:31 AM
 */
public interface LifeCycle {

    public LifeState state();

    public void start();

    public void stop();

    public void suspend();

    public void resume();

}
