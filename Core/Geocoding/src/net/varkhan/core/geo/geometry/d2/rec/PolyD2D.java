package net.varkhan.core.geo.geometry.d2.rec;

import net.varkhan.core.geo.geometry.d2.Point2D;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/23/12
 * @time 12:16 PM
 */
public class PolyD2D extends AbstractShape2D implements Iterable<Point2D> {

    protected int num;
    protected double[] xpts;
    protected double[] ypts;

    protected double x1sm;
    protected double y1sm;
    protected double x2sm;
    protected double y2sm;
    protected double xmin;
    protected double xmax;
    protected double ymin;
    protected double ymax;

    public PolyD2D(double[] xpts, double[] ypts) {
        if(xpts.length!=ypts.length) throw new IllegalArgumentException("X coordinates and Y coordinates must have the same lengths");
        this.num=xpts.length;
        this.xpts=xpts.clone();
        this.ypts=ypts.clone();
        computeInvariants();
    }

    public <P extends Point2D> PolyD2D(P ... pts) {
        this.num=pts.length;
        this.xpts=new double[num];
        this.ypts=new double[num];
        for(int i=0; i<num; i++) {
            this.xpts[i] = pts[i].xctr();
            this.ypts[i] = pts[i].yctr();
        }
        computeInvariants();
    }

    protected void computeInvariants() {
        xmin = ymin = +Double.MAX_VALUE;
        xmax = ymax = -Double.MAX_VALUE;
        x1sm = y1sm = x2sm = y2sm = 0;
        for(int i=0; i<num; i++) {
            double x = xpts[i];
            double y = ypts[i];
            if(xmin>x) xmin=x;
            if(xmax<x) xmax=x;
            if(ymin>y) ymin=y;
            if(ymax<y) ymax=y;
            x1sm += x;
            y1sm += y;
            x2sm += x*x;
            y2sm += y*y;
        }
    }


    public double xmin() { return xmin; }
    public double xmax() { return xmax; }
    public double ymin() { return ymin; }
    public double ymax() { return ymax; }

    @Override
    public double xctr() {
        return x1sm/num;
    }

    @Override
    public double yctr() {
        return y1sm/num;
    }

    @Override
    public Point2D ctr() {
        return new AbstractPoint2D() {
            public double xctr() { return x1sm/num; }
            public double yctr() { return y1sm/num; }
        };
    }

    @Override
    public double rad2() {
        double d = 0;
        double xc = x1sm/num;
        double yc = y1sm/num;
        for(int i=0; i<num; i++) {
            double dx = xpts[i]-xc;
            double dy = ypts[i]-yc;
            double dc = dx*dx+dy*dy;
            if(d<dc) d=dc;
        }
        return d;
    }

    @Override
    public double msr() {
        if(num<=2) return 0;
        double x0 = xpts[0];
        double y0 = ypts[0];
        double x1 = xpts[1];
        double y1 = ypts[1];
        double a = 0;
        for(int i=2; i<num; i++) {
            double x2 = xpts[i];
            double y2 = ypts[i];
            a += area(x0,y0,x1,y1,x2,y2);
            x1 = x2;
            y1 = y2;
        }
        return a<0? -a:a;
    }

    @Override
    public boolean contains(double x, double y) {
        return (winding(x, y, num, xpts, ypts)&1)!=0;
    }

    @Override
    public double dmin2(double x, double y) {
        if(contains(x,y)) return 0.0;
        return distmin2(x, y, num, xpts, ypts);
    }

    @Override
    public double dmax2(double x, double y) {
        return distmax2(x, y, num, xpts, ypts);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('[').append(' ');
        boolean first = true;
        for(int i=0; i<num; i++) {
            if(first) first = false;
            else buf.append(" - ");
            buf.append('(').append(' ').append(xpts[i]).append(' ').append(ypts[i]).append(' ').append(')');
        }
        buf.append(' ').append(']');
        return buf.toString();
    }


    public static int winding(double x, double y, int num, double[] xpts, double[] ypts) {
        if(num<2) return 0;
        // MacMartin's algorithm
        int nc = 0;
        double x0 = xpts[num-1];
        double y0 = ypts[num-1];
        for(int i=0; i<num; i++) {
            double xi = xpts[i];
            double yi = ypts[i];
            if(y0<=y && yi>y) {
                double dx = xi-x0;
                double dy = yi-y0;
                if(((x0-x)*dy-(y0-y)*dx)*dy>0) nc ++;
            }
            else if(y0>=y && yi<y) {
                double dx = xi-x0;
                double dy = yi-y0;
                if(((x0-x)*dy-(y0-y)*dx)*dy>0) nc --;
            }
            x0 = xi;
            y0 = yi;
        }
        return nc;
    }

    public static double distmin2(double x, double y, int num, double[] xpts, double[] ypts) {
        if(num<1) return Double.MAX_VALUE;
        double x0 = xpts[num-1]-x;
        double y0 = ypts[num-1]-y;
        double d0 = x0*x0+y0*y0;
        double d = d0;
        for(int i=0; i<num; i++) {
            double xi = xpts[i]-x;
            double yi = ypts[i]-y;
            double dx = xi-x0;
            double dy = yi-y0;
            double p0 = -(x0*dx+y0*dy);
//            double pi = xi*dx+yi*dy;
            double di = xi*xi+yi*yi;
            double dd = dx*dx+dy*dy;
            if(p0>0 && p0<dd) {
                double t = p0/dd;
                double tx = x0 + t*dx;
                double ty = y0 + t*dy;
                double dl = tx*tx+ty*ty;
//                System.err.println("("+xi+","+yi+") <"+d0+","+di+"> ["+dl+"@"+tx+","+ty+"] "+p0+" "+dd);
                assert dl<d0 && dl<di: "This can't happen: dist to segment "+dl+" > dist to endpoints "+d0+" | "+di;
                if(d>dl) d=dl;
            }
            else {
//                System.err.println("("+xi+","+yi+") <"+d0+","+di+"> "+p0+" "+dd);
                if(d>di) d = di;
            }
            x0 = xi;
            y0 = yi;
            d0 = di;
        }
        return d;
    }

    public static double distmax2(double x, double y, int num, double[] xpts, double[] ypts) {
        double d = 0;
        for(int i=0; i<num; i++) {
            double xi = xpts[i]-x;
            double yi = ypts[i]-y;
            double di = xi*xi+yi*yi;
            if(d<di) d = di;
        }
        return d;
    }

    public static double area(double x0, double y0, double x1, double y1, double x2, double y2) {
        x1 -= x0;
        y1 -= y0;
        x2 -= x0;
        y2 -= y0;
        double dx = x2-x1;
        double dy = y2-y1;
        double d0 = dx*dx+dy*dy;
        double d1 = x1*x1+y1*y1;
        double d2 = x2*x2+y2*y2;
        // Derived from Heron's formula
//        double s = d0+d1+d2;
//        double a = s*s - 2 * (d0*d0+d1*d1+d2*d2);
        // Careful of numerical drift here... it's easy to get a<0 with the wrong sequence like above
        double a = 2*d0*d1 + 2*d1*d2 + 2*d2*d0 - d0*d0 - d1*d1 - d2*d2;
        double d = 0.25 * Math.sqrt(a);
        double z = x1*y2-x2*y1;
        return (z>=0) ? d:-d;
    }

    public static double angle(double x0, double y0, double x1, double y1, double x2, double y2) {
        x1 -= x0;
        y1 -= y0;
        x2 -= x0;
        y2 -= y0;
        double d1 = x1*x1 + y1*y1;
        double d2 = x2*x2 + y2*y2;
        double dp = x1*x2 + y1*y2;
        double a = Math.acos(dp/Math.sqrt(d1 * d2));
        double z = x1*y2-x2*y1;
        return (z>=0) ? a:-a;
    }

    @Override
    public Iterator<Point2D> iterator() {
        return new Iterator<Point2D>() {
            private volatile int pos=0;
            @Override
            public boolean hasNext() { return pos<num; }
            @Override
            public Point2D next() { PointD2D p = new PointD2D(xpts[pos],ypts[pos]); pos ++; return p; }
            @Override
            public void remove() { }
        };
    }

}
