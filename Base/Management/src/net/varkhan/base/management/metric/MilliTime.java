/**
 *
 */
package net.varkhan.base.management.metric;

/**
 * <b>.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 8:24:05 PM
 */
public class MilliTime implements Time {

    /**
     * The current time in milliseconds (as per {@link System#currentTimeMillis()}.
     *
     * @return the value of {@code System.currentTimeMillis()}
     */
    public long time() {
        return System.currentTimeMillis();
    }

}
