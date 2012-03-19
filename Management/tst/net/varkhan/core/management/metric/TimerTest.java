/**
 *
 */
package net.varkhan.core.management.metric;

import junit.framework.TestCase;

/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 25, 2010
 * @time 8:30:46 PM
 */
public class TimerTest extends TestCase {

    private final int verbosity = 0;

    public void testExactTimer() {
        Time t = new NanoTime();
        Timer tm = new ExactTimer(t);
        testTimer(tm,10*1000*1000*1000L,t,10,50*1000*1000,verbosity);
        profTimer(tm,10*1000*1000);
        t = new MilliTime();
        tm = new ExactTimer(t);
        testTimer(tm,10*1000,t,10,50,verbosity);
        profTimer(tm,10);
    }

    public void testSteppingTimer() {
        Time t = new NanoTime();
        Timer tm = new SteppingTimer(t,10);
        testTimer(tm,10*1000*1000*1000L,t,10,50*1000*1000,verbosity);
        profTimer(tm,10*1000*1000);
        t = new MilliTime();
        tm = new SteppingTimer(t,10);
        testTimer(tm,10*1000,t,10,50,verbosity);
        profTimer(tm,10);
    }

    public void testSamplingTimer() {
        Time t = new NanoTime();
        Timer tm = new SamplingTimer(t,10);
        testTimer(tm,10*1000*1000*1000L,t,10,50*1000*1000,verbosity);
        profTimer(tm,10*1000*1000);
        t = new MilliTime();
        tm = new SamplingTimer(t,10);
        testTimer(tm,10*1000,t,10,50,verbosity);
        profTimer(tm,10);
    }


    private static void testTimer(Timer tm, long runtime, Time rf, long sampling, long range, int verbosity) {
        long t0 = rf.time();
        long t;
        tm.reset();
        tm.start();
        long mind = 0, maxd = 0;
        while((t=rf.time())<t0+runtime) {
            long et = tm.time();
            if(verbosity>1) System.out.println("Current time: "+(t-t0)+" | "+et);
            long d = et-t+t0;
            if(mind>d) mind = d;
            if(maxd<d) maxd = d;
            assertTrue(tm.getClass().getSimpleName()+" current time: "+(t-t0)+" | "+et,(d<2*range)&&(d>-2*range));
            try {
                Thread.sleep((long)(sampling*Math.random()));
            }
            catch(InterruptedException e) {
                // bleh
            }
        }
        long et = tm.time();
        if(verbosity>0) System.out.println(tm.getClass().getSimpleName()+" current time: "+(t-t0)+" | "+et);
        System.out.println(tm.getClass().getSimpleName()+" variation "+mind+" | "+maxd);
    }

    private static void profTimer(Timer tm, long runtime) {
        tm.reset();
        tm.start();
        long count = 0;
        while(true) {
            count ++;
            if(tm.time()>9*runtime) break;
        }
        tm.reset();
        tm.start();
        long t0 = System.nanoTime();
        count = 0;
        while(true) {
            count ++;
            if(tm.time()>runtime) break;
        }
        long t1 = System.nanoTime();
        System.out.println(tm.getClass().getSimpleName()+" time/call "+(t1-t0)/count+" ns");
    }
}
