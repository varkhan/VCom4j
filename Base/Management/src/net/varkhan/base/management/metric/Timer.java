/**
 *
 */
package net.varkhan.base.management.metric;

/**
 * <b>A time elapsed source.</b>
 * <p/>
 * This interface expands the contract provided by {@link Time} by providing ways
 * to reset the origin of time to a specific instant (so that the value of time at
 * that instant is 0) and stop and start the increase of time (so that the value of
 * time at the instant of start is the same as its value at the last stop or reset).
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 7:31:02 PM
 */
public interface Timer extends Time {

    /**
     * The current time elapsed.
     *
     * @return the total time elapsed between calls to {@link #start()} and {@link #stop()} since the last call to {@link #reset()}
     */
    public long time();

    public void start();

    public void stop();

    public void reset();


}
