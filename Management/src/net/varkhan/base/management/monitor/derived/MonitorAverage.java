/**
 *
 */
package net.varkhan.base.management.monitor.derived;

import net.varkhan.base.management.monitor.MonitorAggregate;
import net.varkhan.base.management.monitor.primitive.MonitorDouble;
import net.varkhan.base.management.monitor.primitive.MonitorLong;

import java.io.Serializable;


/**
 * @author varkhan
 * @date Jun 16, 2009
 * @time 11:54:38 PM
 */
public abstract class MonitorAverage implements MonitorDouble, MonitorAggregate<MonitorAverage.MinMaxAvg,Double,Double>, Serializable {

    private static final long serialVersionUID=1L;


    public enum MinMaxAvg {
        /**
         * The number of frames (updates, time slices, events) elapsed since the last reset
         */
        FRAMES,
        /**
         * The global minimum value (since the last update)
         */
        GLBMIN,
        /**
         * The global maximum value (since the last update)
         */
        GLBMAX,
        /**
         * The global average value (since the last update)
         */
        GLBAVG,
        /**
         * The global variance value (since the last update)
         */
        GLBVAR,
        /**
         * The local minimum value (within the local set of frames)
         */
        LOCMIN,
        /**
         * The local maximum value (within the local set of frames)
         */
        LOCMAX,
        /**
         * The local average value (within the local set of frames)
         */
        LOCAVG,
        /**
         * The local variance value (within the local set of frames)
         */
        LOCVAR,

    }


    protected final         MonitorLong     frame;
    protected final         MonitorDouble   value;
    protected               double          glbmin;
    protected               double          glbmax;
    protected               double          glbavg;
    protected               double          glbvar;
    protected               double          locmin;
    protected               double          locmax;
    protected               double          locavg;
    protected               double          locvar;
    private transient final MonitorDouble[] indicators;

    protected MonitorAverage(MonitorLong frame, MonitorDouble value) {
        this.frame=frame;
        this.value=value;
        abstract class MValue implements MonitorDouble {
            public Class<Double> type() { return Double.class; }

            public void reset() { MonitorAverage.this.reset(); }

            public void update() { MonitorAverage.this.update(); }
        }
        this.indicators=new MonitorDouble[MinMaxAvg.values().length];
        this.indicators[MinMaxAvg.FRAMES.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.frame.value().doubleValue(); }
        };
        this.indicators[MinMaxAvg.GLBMIN.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.glbmin; }
        };
        this.indicators[MinMaxAvg.GLBMAX.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.glbmax; }
        };
        this.indicators[MinMaxAvg.GLBAVG.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.glbavg; }
        };
        this.indicators[MinMaxAvg.GLBVAR.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.glbvar; }
        };
        this.indicators[MinMaxAvg.LOCMIN.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.locmin; }
        };
        this.indicators[MinMaxAvg.LOCMAX.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.locmax; }
        };
        this.indicators[MinMaxAvg.LOCAVG.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.locavg; }
        };
        this.indicators[MinMaxAvg.LOCVAR.ordinal()]=new MValue() {
            public Double value() { return MonitorAverage.this.locvar; }
        };
    }


    public Class<Double> type() { return Double.class; }


    public void reset() {
        frame.reset();
        value.reset();
        glbmin=+Double.MAX_VALUE;
        glbmax=-Double.MAX_VALUE;
        glbavg=0;
        glbvar=0;
        locmin=+Double.MAX_VALUE;
        locmax=-Double.MAX_VALUE;
        locavg=0;
        locvar=0;
    }

    public void update() {
        frame.update();
        value.update();
    }

    /**
     * The number of frames elapsed since the last reset.
     *
     * @return the frame count
     */
    public final long frame() { return frame.value(); }

    public final Double value() { return value.value(); }

    public final MinMaxAvg[] components() { return MinMaxAvg.values(); }

    public final MonitorDouble component(MinMaxAvg c) { return indicators[c.ordinal()]; }

    public final MonitorDouble component(String n) { return indicators[MinMaxAvg.valueOf(n).ordinal()]; }

    public final Double value(MinMaxAvg c) { return indicators[c.ordinal()].value(); }

    public final Double value(String n) { return indicators[MinMaxAvg.valueOf(n).ordinal()].value(); }

    public String toString() { return Double.toString(value()); }

}
