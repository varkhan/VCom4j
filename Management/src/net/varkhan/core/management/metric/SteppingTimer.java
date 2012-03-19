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
 * @time 7:56:21 PM
 */
public class SteppingTimer implements Timer {

    private final    Time time;
    private final    long rate;
    private volatile long tick;
    private volatile long curr;
    private volatile long start;
    private volatile long stop;

    public SteppingTimer(Time time, long rate) {
        this.time=time;
        this.rate=rate;
        reset();
    }

    public void reset() {
        curr=time.time();
        // Stack the happens-before statements in the right order: mark as stopped, then update the start time, then ticks
        stop=curr;
        start=curr;
        tick=0;
    }

    public void start() {
        if(start<=stop) {
            curr=time.time();
            long delta=curr-stop;
            // Stack the happens-before statements in the right order: mark as started, then update the start time, then ticks
            // curr >= stop >= start   =>   start + curr - stop >= start  =>  start > stop after update
            stop=start-1;
            start+=delta;
            tick=0;
        }
    }

    public void stop() {
        stop=time.time();
    }

    public long time() {
        if(start<stop) return stop-start;
        if(++tick>rate) {
            curr=time.time();
            tick=0;
            return curr-start;
        }
        return curr-start;
    }

}
