package net.varkhan.core.geo.geometry.plane;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/16/14
 * @time 12:34 PM
 */
public class UnionShape2D extends AbstractShape2D {

    protected Shape2D[] shapes;

    public <S extends Shape2D> UnionShape2D(S... shapes) {
        this.shapes=shapes;
    }

    @Override
    public int dim() {
        return 2;
    }

    @Override
    public double xmin() {
        double xmin=Double.MAX_VALUE;
        for(Shape2D s: shapes) {
            double sxmin = s.xmin();
            if(xmin>sxmin) xmin=sxmin;
        }
        return xmin;
    }

    @Override
    public double ymin() {
        double ymin=Double.MAX_VALUE;
        for(Shape2D s: shapes) {
            double symin = s.ymin();
            if(ymin>symin) ymin=symin;
        }
        return ymin;
    }

    @Override
    public double xmax() {
        double xmax=-Double.MAX_VALUE;
        for(Shape2D s: shapes) {
            double sxmax = s.xmax();
            if(xmax>sxmax) xmax=sxmax;
        }
        return xmax;
    }

    @Override
    public double ymax() {
        double ymax=-Double.MAX_VALUE;
        for(Shape2D s: shapes) {
            double symax = s.ymax();
            if(ymax>symax) ymax=symax;
        }
        return ymax;
    }

    @Override
    public double xctr() {
        double x=0;
        double m=0;
        for(Shape2D s: shapes) {
            double sx = s.xctr();
            double sm = s.msr();
            x += sm*sx;
            m += sm;
        }
        if(m<=0) return 0;
        return x/m;
    }

    @Override
    public double yctr() {
        double y=0;
        double m=0;
        for(Shape2D s: shapes) {
            double sy = s.yctr();
            double sm = s.msr();
            y += sm*sy;
            m += sm;
        }
        if(m<=0) return 0;
        return y/m;
    }

    @Override
    public double rad2() {
        // No exact answer can be computed here on generic shapes.
        // We will just use:
        //  - the radius of the bounding box  = O(n)
        //  - the max of center distances plus radius = O(n^2) (which we hope is interrupted early enough)
        // (whichever is smallest)
        double xmin = Double.MAX_VALUE;
        double xmax = -Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double ymax = -Double.MAX_VALUE;
        for(Shape2D s: shapes) {
            double sxmin = s.xmin();
            if(xmin>sxmin) xmin=sxmin;
            double symin = s.ymin();
            if(ymin>symin) ymin=symin;
            double sxmax = s.xmax();
            if(xmax<sxmax) xmax=sxmax;
            double symax = s.ymax();
            if(ymax<symax) ymax=symax;
        }
        double dx = xmax-xmin;
        double dy = ymax-ymin;
        double bbr2 = 0.25 * ( dx*dx + dy*dy );
        double cdr2 = 0;
        for(Shape2D s1: shapes) {
            double r1=s1.rad2();
            Point2D c1=s1.ctr();
            for(Shape2D s2: shapes) {
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
        for(Shape2D s: shapes) {
            m += s.msr();
        }
        return m;
    }

    @Override
    public boolean contains(Point2D point) {
        for(Shape2D s: shapes) {
            if(s.contains(point)) return true;
        }
        return false;
    }

    @Override
    public boolean contains(double x, double y) {
        for(Shape2D s: shapes) {
            if(s.contains(x,y)) return true;
        }
        return false;
    }

    @Override
    public double dmin2(double x, double y) {
        double d = 0;
        for(Shape2D s: shapes) {
            double sd = s.dmin2(x,y);
            if(d>sd) d=sd;
        }
        return d;
    }

    @Override
    public double dmax2(double x, double y) {
        double d = Double.MAX_VALUE;
        for(Shape2D s: shapes) {
            double sd = s.dmin2(x,y);
            if(d<sd) d=sd;
        }
        return d;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('}').append(' ');
        boolean first = true;
        for(Shape2D s: shapes) {
            if(first) first = false;
            else buf.append(" U ");
            buf.append(s.toString());
        }
        buf.append(' ').append('}');
        return buf.toString();
    }

}
