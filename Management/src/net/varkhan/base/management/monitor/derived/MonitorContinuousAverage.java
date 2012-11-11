/**
 *
 */
package net.varkhan.base.management.monitor.derived;

import net.varkhan.base.management.monitor.primitive.MonitorDouble;
import net.varkhan.base.management.monitor.primitive.MonitorLong;


/**
 * @author varkhan
 * @date Jun 16, 2009
 * @time 11:54:07 PM
 */
public class MonitorContinuousAverage extends MonitorAverage {

    private static final long serialVersionUID=1L;

    private final long halflife;
    private       long lastupdt;

    public MonitorContinuousAverage(MonitorLong frame, MonitorDouble value, long halflife) {
        super(frame, value);
        this.halflife=halflife;
    }

    public void reset() {
        super.reset();
        lastupdt=super.frame();
    }


    /**
     * *******************************************************************************
     * *  Continuous update
     */

    public void update() {
        super.update();
        long frame=super.frame();
        double value=super.value();
        if(frame<=lastupdt) return;
        // Local averages are subject to an exponential attrition factor, dividing by two every half-life frames
        double oldf=decay(frame-lastupdt, halflife);
        double newf=1.0-oldf;
        locmin=(float) (oldf*locmin+newf*locavg);
        locmax=(float) (oldf*locmax+newf*locavg);
        locavg=(float) (oldf*locavg+newf*value);
        locvar=(float) (oldf*locvar+newf*(value*value-locavg*locavg));
        if(locmin>value) locmin=(float) value;
        if(locmax<value) locmax=(float) value;
        if(glbmin>value) glbmin=(float) value;
        if(glbmax<value) glbmax=(float) value;
        // Global averages only depend on elapsed frames
        double dlframes=frame-lastupdt;
        double dlavg=value-glbavg;
        double dlvar=value*value-glbvar;
        glbavg+=dlavg*dlframes/frame;
        glbvar+=(dlvar-(dlavg*dlavg*dlframes/frame))*dlframes/frame;
        lastupdt=frame;
    }


    /**
     * *******************************************************************************
     * *  Static math helper methods
     */

    private static final float   FP_Ln2  =(float) Math.log(2.0);
    private static final int     valscale=10;
    private static final int     valcount=1<<valscale;
    private static final float[] expvals =new float[valcount+1];

    static {
        double c=1.0f/valcount;
        double f=0.0f;
        for(int i=0;i<=valcount;i++) {
            expvals[i]=(float) Math.exp(f*FP_Ln2);
            f-=c;
        }
    }

    private static double decay(long d, long h) {
        int x=(int) (d/h);
        int f=(int) ((d-x*h)*valcount/h);
        return Math.scalb(expvals[f], -x);
    }

}
