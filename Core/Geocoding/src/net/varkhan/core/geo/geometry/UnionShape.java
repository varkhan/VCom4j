package net.varkhan.core.geo.geometry;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/16/14
 * @time 12:34 PM
 */
public class UnionShape extends AbstractShape {

    protected int dim;
    protected Shape[] shapes;

    public <S extends Shape> UnionShape(int dim, S... shapes) {
        this.dim=dim;
        for(Shape s: shapes) if(s.dim()!=dim)
            throw new IllegalArgumentException("Illegal shape of dimension "+s.dim()+" in a "+dim+"-dimensional union");
        this.shapes=shapes;
    }

    @Override
    public int dim() {
        return dim;
    }

    @Override
    public double[] cmin() {
        double[] c = new double[dim];
        boolean first = true;
        for(Shape s: shapes) {
            double[] sc = s.cmin();
            // sc.length should be equal to dim, but in the case it isn't, extra dimensions have 0.0 coordinates
            if(sc!=null) for(int i=0; i<dim && i<sc.length; i++) {
                if(first || c[i]>sc[i]) c[i]=sc[i];
            }
            first = false;
        }
        return c;
    }

    @Override
    public double[] cmax() {
        double[] c = new double[dim];
        boolean first = true;
        for(Shape s: shapes) {
            double[] sc = s.cmax();
            // sc.length should be equal to dim, but in the case it isn't, extra dimensions have 0.0 coordinates
            if(sc!=null) for(int i=0; i<dim && i<sc.length; i++) {
                if(first || c[i]<sc[i]) c[i]=sc[i];
            }
            first = false;
        }
        return c;
    }

    @Override
    public double[] cctr() {
        double[] c = new double[dim];
        double m=0;
        for(Shape s: shapes) {
            double[] sc = s.cctr();
            double sm = s.msr();
            if(sc!=null) for(int i=0; i<dim && i<sc.length; i++) {
                c[i] += sm*sc[i];
            }
            m += sm;
        }
        if(m<=0) return c;
        m = 1.0/m;
        for(int i=0; i<dim; i++) c[i] *= m;
        return c;
    }

    @Override
    public double rad2() {
        // No exact answer can be computed here on generic shapes.
        // We will just use:
        //  - the radius of the bounding box  = O(n)
        //  - the max of center distances plus radius = O(n^2) (which we hope is interrupted early enough)
        // (whichever is smallest)
        double[] cmin = new double[dim];
        double[] cmax = new double[dim];
        boolean first = true;
        for(Shape s: shapes) {
            // scmin.length and scmax.length should be equal to dim, but in the case they aren't, extra dimensions have 0.0 coordinates
            double[] scmin = s.cmin();
            if(scmin!=null) for(int i=0; i<dim && i<scmin.length; i++) {
                if(first || cmin[i]>scmin[i]) cmin[i]=scmin[i];
            }
            double[] scmax = s.cmax();
            if(scmax!=null) for(int i=0; i<dim && i<scmax.length; i++) {
                if(first || cmax[i]<scmax[i]) cmax[i]=scmax[i];
            }
            first = false;
        }
        double bbr2 = 0;
        for(int i=0; i<dim; i++) {
            double d = cmax[i]-cmin[i];
            bbr2 += d*d;
        }
        bbr2 = 0.25 * bbr2;
        double cdr2 = 0;
        for(Shape s1: shapes) {
            double r1=s1.rad2();
            Point c1=s1.ctr();
            for(Shape s2: shapes) {
                double r2=s2.rad2();
                double d2 = r1 + c1.dmin2(s2.ctr()) + r2;
                if(d2>bbr2) return bbr2;
                else if(cdr2<d2) cdr2=d2;
            }
        }
        return cdr2;
    }

    @Override
    public double msr() {
        double m=0;
        for(Shape s: shapes) {
            m += s.msr();
        }
        return m;
    }

    @Override
    public boolean contains(Point point) {
        for(Shape s: shapes) {
            if(s.contains(point)) return true;
        }
        return false;
    }

    @Override
    public boolean contains(double... point) {
        for(Shape s: shapes) {
            if(s.contains(point)) return true;
        }
        return false;
    }

    @Override
    public double dmin2(double... point) {
        double d = 0;
        for(Shape s: shapes) {
            double sd = s.dmin2(point);
            if(d>sd) d=sd;
        }
        return d;
    }

    @Override
    public double dmax2(double... point) {
        double d = Double.MAX_VALUE;
        for(Shape s: shapes) {
            double sd = s.dmin2(point);
            if(d<sd) d=sd;
        }
        return d;
    }
}
