package net.varkhan.core.geo.shape;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 3:31 PM
 */
public class RectD extends AbstractShape implements Rect {

    protected final int dim;
    protected double[] cmin;
    protected double[] cmax;

    public RectD(double[] cmin, double[] cmax) {
        if(cmin.length!=cmax.length) throw new IllegalArgumentException("Min and max coordinate vectors must have the same dimension");
        this.dim=cmin.length;
        this.cmin=cmin.clone();
        this.cmax=cmax.clone();
    }

    public RectD(Shape shape) {
        this(shape.cmin(), shape.cmax());
    }

    public RectD(Point point, double[] cdel) {
        this.dim=point.dim();
        if(cdel.length!=this.dim) throw new IllegalArgumentException("Center coordinate and variation vectors must have the same dimension");
        this.cmin=new double[this.dim];
        this.cmax=new double[this.dim];
        for(int d=0; d<dim; d++) {
            this.cmin[d] = point.cctr(d) - cdel[d];
            this.cmax[d] = point.cctr(d) + cdel[d];
        }
    }

    public RectD(Point pmin, Point pmax) {
        if(pmin.dim()!=pmax.dim()) throw new IllegalArgumentException("Min and max points must have the same dimension");
        this.dim=pmin.dim();
        this.cmin=new double[this.dim];
        this.cmax=new double[this.dim];
        for(int d=0; d<dim; d++) {
            this.cmin[d] = pmin.cctr(d);
            this.cmax[d] = pmax.cctr(d);
        }
    }

    @Override
    public int dim() { return dim; }
    public double[] cmin() { return cmin.clone(); }
    public double[] cmax() { return cmax.clone(); }
    public double cmin(int d) { return d<dim() ? cmin[d] : 0; }
    public double cmax(int d) { return d<dim() ? cmax[d] : 0; }

    @Override
    public Point ctr() { return new AbstractPoint() {
        public double cctr(int d) { return RectD.this.cctr(d); }
        public double[] cctr() { return RectD.this.cctr(); }
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
        for(int d=0; d<this.dim; d++) {
            double v = cmax[d]-cmin[d];
            m*=v;
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
        int h = 0;
        for(double c: cmin) {
            long bits = Double.doubleToRawLongBits(c);
            h = 31 * h + (int)(bits ^ (bits >>> 32));
        }
        for(double c: cmax) {
            long bits = Double.doubleToRawLongBits(c);
            h = 61 * h + (int)(bits ^ (bits >>> 32));
        }
        return h;
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

}
