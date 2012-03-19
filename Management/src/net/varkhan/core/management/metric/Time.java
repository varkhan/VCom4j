/**
 *
 */
package net.varkhan.core.management.metric;

/**
 * <b>A time source.</b>
 * <p/>
 * The {@link #time()} method provide a measure of elapsed time, for some arbitrary
 * notion of <em>time</em> and since some arbitrary origin.
 * <p/>
 * More formally, the only contract on this method is that on any call, it returns
 * values that are not smaller than any value returned by a previous call.
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 8:09:35 PM
 */
public interface Time {

    /**
     * The current time.
     * <p/>
     * The only constraint on this method is that a call of the method will
     * return a value that is not smaller than any value returned by a previous
     * call to this method.
     *
     * @return the current time
     */
    public long time();

}
