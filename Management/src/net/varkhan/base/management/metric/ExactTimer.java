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
 * @time 8:27:31 PM
 */
public class ExactTimer implements Timer {

    private final    Time time;
    private volatile long start;
    private volatile long stop;

    public ExactTimer(Time time) {
        this.time=time;
    }

    public void reset() {
        long curr=time.time();
        stop=curr;
        start=curr;
    }

    public void start() {
        if(start<=stop) {
            long curr=time.time();
            long delta=curr-stop;
            stop=start-1;
            start+=delta;
        }
    }

    public void stop() {
        stop=time.time();
    }

    public long time() {
        return time.time()-start;
    }

}
