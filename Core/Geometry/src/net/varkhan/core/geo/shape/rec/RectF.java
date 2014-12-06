package net.varkhan.core.geo.shape.rec;


import net.varkhan.core.geo.shape.Point;
import net.varkhan.core.geo.shape.Rect;
import net.varkhan.core.geo.shape.Shape;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 3:31 PM
 */
public class RectF extends AbstractShape implements Rect {

    protected final int dim;
    protected float[] cmin;
    protected float[] cmax;

    public RectF(float[] cmin, float[] cmax) {
        if(cmin.length!=cmax.length) throw new IllegalArgumentException("Min and max coordinate vectors must have the same dimension");
        this.dim=cmin.length;
        this.cmin=cmin.clone();
        this.cmax=cmax.clone();
    }

    public RectF(Shape shape) {
        this.dim=shape.dim();
        this.cmin=new float[this.dim];
        this.cmax=new float[this.dim];
        for(int d=0; d<dim; d++) {
            this.cmin[d] = (float)shape.cmin(d);
            this.cmax[d] = (float)shape.cmax(d);
        }
    }

    public RectF(Point point, double... cdel) {
        this.dim=point.dim();
        if(cdel.length!=this.dim) throw new IllegalArgumentException("Center coordinate and variation vectors must have the same dimension");
        this.cmin=new float[this.dim];
        this.cmax=new float[this.dim];
        for(int d=0; d<dim; d++) {
            this.cmin[d] = (float)(point.cctr(d) - cdel[d]);
            this.cmax[d] = (float)(point.cctr(d) + cdel[d]);
        }
    }

    public RectF(Point pmin, Point pmax) {
        if(pmin.dim()!=pmax.dim()) throw new IllegalArgumentException("Min and max points must have the same dimension");
        this.dim=pmin.dim();
        this.cmin=new float[this.dim];
        this.cmax=new float[this.dim];
        for(int d=0; d<dim; d++) {
            this.cmin[d] = (float)pmin.cctr(d);
            this.cmax[d] = (float)pmax.cctr(d);
        }
    }

    @Override
    public int dim() { return dim; }
    public double[] cmin() {
        return clone(cmin, dim);
    }

    public double[] cmax() {
        return clone(cmax, dim);
    }

    public double cmin(int d) { return d<dim() ? cmin[d] : 0; }
    public double cmax(int d) { return d<dim() ? cmax[d] : 0; }

    @Override
    public Point ctr() { return new AbstractPoint() {
        public double cctr(int d) { return RectF.this.cctr(d); }
        public double[] cctr() { return RectF.this.cctr(); }
    }; }

    @Override
    public double[] cctr() {
        double[] c = new double[dim];
        for(int d=0; d<dim; d++) {
            c[d] = 0.5*(cmin[d]+cmax[d]);
        }
        return c;
    }

    @Override
    public double cctr(int d) { return d<dim() ? 0.5*(cmin[d]+cmax[d]) : 0; }

    @Override
    public double rad2() {
        double r2 = 0.0;
        for(int d=0; d<dim; d++) {
            double v = cmax[d]-cmin[d];
            r2 += v*v;
        }
        return 0.25*r2;
    }

    @Override
    public double msr() {
        double m = 1.0;
        for(int d=0; d<dim; d++) {
            double v = cmax[d]-cmin[d];
            m *= v;
        }
        return m;
    }

    @Override
    public double msr(int dd) {
        if(dd<=0) return Double.POSITIVE_INFINITY;
        if(dd>this.dim) return 0;
        double m = 1.0;
        // *Effective* dimension (i.e. number of non-trivial sides)
        int de=0;
        for(int d=0; d<this.dim; d++) {
            double v = cmax[d]-cmin[d];
            if(v!=0) de++;
            else m*=v;
        }
        if(dd>de) return 0;
        // hyper-volume
        if(dd==de) return m;
        // hyper-surface O(dim^2)
        if(dd==de-1) {
            m=0;
            for(int i=0; i<this.dim; i++) {
                double mm = 1.0;
                for(int d=0; d<this.dim; d++) {
                    double v = cmax[d]-cmin[d];
                    if(i!=d) m*=v;
                }
                m += mm;
            }
            return 2*m;
        }
        // hyper-length and below
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double dmin2(double... c) {
        double r2 = 0.0;
        for(int d=0; d<dim; d++) {
            double v=c[d];
            double vmin=cmin[d];
            double vmax=cmax[d];
            double dv = 0;
            if(vmin>v) dv = vmin-v;
            if(v>vmax) dv = v-vmax;
            r2 += dv*dv;
        }
        return r2;
    }

    @Override
    public double dmax2(double... c) {
        double r2 = 0.0;
        for(int d=0; d<dim; d++) {
            double v=c[d];
            double vmin=cmin[d];
            double vmax=cmax[d];
            double dv=0;
            double dvmin=vmin-v;
            if(dv<+dvmin) dv=+dvmin;
            if(dv<-dvmin) dv=-dvmin;
            double dvmax=v-vmax;
            if(dv<+dvmax) dv=+dvmax;
            if(dv<-dvmax) dv=-dvmax;
            r2+=dv*dv;
        }
        return r2;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof Rect){
            Rect that=(Rect) o;
            if(dim!=that.dim()) return false;
            for(int d=0; d<dim; d++) {
                if(cmin[d]!=that.cmin(d)) return false;
                if(cmax[d]!=that.cmax(d)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return AbstractPoint.hashCode(cmin) + 31* AbstractPoint.hashCode(cmax);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ');
        boolean f=true;
        for(int d=0; d<dim; d++) {
            if(f) f=false;
            else buf.append(" x ");
            buf.append('[').append(cmin[d]).append(':').append(cmax[d]).append(']');
        }
        buf.append(' ').append(')');
        return buf.toString();
    }

    protected static float[] clone(double[] cc, int dd) {
        float[] c = new float[dd];
        for(int d=0; d<dd; d++) {
            c[d] = (float)cc[d];
        }
        return c;
    }

    protected static double[] clone(float[] cc, int dd) {
        double[] c = new double[dd];
        for(int d=0; d<dd; d++) {
            c[d] = cc[d];
        }
        return c;
    }

}
