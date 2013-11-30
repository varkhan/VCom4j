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
 * @time 7:36:03 PM
 */
public class SamplingTimer implements Timer {

    private final    Time time;
    private final    long rate;
    private volatile long tick;
    private volatile long curr;
    private volatile long last;
    private volatile long start;
    private volatile long stop;


    public SamplingTimer(Time time, long rate) {
        this.time=time;
        this.rate=rate;
        reset();
    }

    public void reset() {
        last=curr=time.time();
        // Stack the happens-before statements in the right order: mark as stopped, then update the start time, then ticks
        stop=curr;
        start=curr;
        tick=0;
    }

    public void start() {
        if(start<=stop) {
            last=curr;
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
        if(++tick>=rate) {
            last=curr;
            curr=time.time();
            tick=0;
            return curr-start;
        }
        return curr-start+(curr-last)*tick/rate;
    }

}
