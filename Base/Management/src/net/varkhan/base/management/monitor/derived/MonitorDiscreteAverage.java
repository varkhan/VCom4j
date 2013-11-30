/**
 *
 */
package net.varkhan.base.management.monitor.derived;

import net.varkhan.base.management.monitor.primitive.MonitorDouble;
import net.varkhan.base.management.monitor.primitive.MonitorLong;


/**
 * @author varkhan
 * @date Jun 17, 2009
 * @time 3:24:12 AM
 */
public class MonitorDiscreteAverage extends MonitorAverage {

    private static final long serialVersionUID=1L;

    private       int      lastframe;
    private final long[]   framespan;
    private final double[] valuespan;
    private final int      histspan;
    private final double   histfact;

    /**
     * @param frame
     * @param value
     */
    protected MonitorDiscreteAverage(MonitorLong frame, MonitorDouble value, int histsize, int histspan) {
        super(frame, value);
        this.lastframe=0;
        this.framespan=new long[histsize];
        this.valuespan=new double[histsize];
        this.histspan=histspan;
        this.histfact=1.0/histspan;
    }


    /**
     * *******************************************************************************
     * *  Continuous update
     */

    public void update() {
        super.update();
        long frame=super.frame();
        double value=super.value();
        framespan[lastframe]=frame;
        valuespan[lastframe]=value;
        lastframe++;
        if(lastframe>=framespan.length) lastframe-=framespan.length;
        locmin=+Double.MAX_VALUE;
        locmax=-Double.MAX_VALUE;
        locavg=0;
        locvar=0;
        long prevframe=0;
        for(int i=0;i<framespan.length;i++) {
            long f=framespan[i];
            if(f<=frame-histspan) continue;
            if(f<frame&&f>prevframe) prevframe=f;
            double v=valuespan[i];
            if(locmin>v) locmin=v;
            if(locmax<v) locmax=v;
            if(glbmin>v) glbmin=v;
            if(glbmax<v) glbmax=v;
            locavg+=v;
            locvar+=v*v;
        }
        locavg*=histfact;
        locvar*=histfact;
        double glbfact=(frame-prevframe)/(double) frame;
        glbavg+=(value-glbavg)*glbfact;
        glbvar+=(value*value-glbvar)*glbfact;
    }

}
