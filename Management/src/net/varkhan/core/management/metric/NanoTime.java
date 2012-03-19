/**
 *
 */
package net.varkhan.core.management.metric;

/**
 * <b>.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 8:24:05 PM
 */
public class NanoTime implements Time {

    /**
     * The current time in milliseconds (as per {@link System#nanoTime()}.
     *
     * @return the value of {@code System.nanoTime()}
     */
    public long time() {
        return System.nanoTime();
    }

}
