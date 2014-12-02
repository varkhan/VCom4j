package net.varkhan.core.geo.shape.d2.rec;

import net.varkhan.core.geo.shape.d2.Point2D;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/23/12
 * @time 12:16 PM
 */
public class PolyP2D extends AbstractShape2D implements Iterable<Point2D>  {

    protected Point2D[] pts;

    protected double x1sm;
    protected double y1sm;
    protected double x2sm;
    protected double y2sm;
    protected double xmin;
    protected double xmax;
    protected double ymin;
    protected double ymax;

    public PolyP2D(double[] xpts, double[] ypts) {
        if(xpts.length!=ypts.length) throw new IllegalArgumentException("X coordinates and Y coordinates must have the same lengths");
        this.pts=new Point2D[xpts.length];
        for(int i=0; i<xpts.length && i<ypts.length; i++) {
            pts[i] = new PointD2D(xpts[i],ypts[i]);
        }
        computeInvariants();
    }

    public PolyP2D(float[] xpts, float[] ypts) {
        if(xpts.length!=ypts.length) throw new IllegalArgumentException("X coordinates and Y coordinates must have the same lengths");
        this.pts=new Point2D[xpts.length];
        for(int i=0; i<xpts.length && i<ypts.length; i++) {
            pts[i] = new PointF2D(xpts[i],ypts[i]);
        }
        computeInvariants();
    }

    public <P extends Point2D> PolyP2D(P... pts) {
        this.pts=pts.clone();
        computeInvariants();
    }

    protected void computeInvariants() {
        xmin = ymin = +Double.MAX_VALUE;
        xmax = ymax = -Double.MAX_VALUE;
        x1sm = y1sm = x2sm = y2sm = 0;
        for(int i=0; i<pts.length; i++) {
            double x = pts[i].xctr();
            double y = pts[i].yctr();
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
        return x1sm/pts.length;
    }

    @Override
    public double yctr() {
        return y1sm/pts.length;
    }

    @Override
    public Point2D ctr() {
        return new AbstractPoint2D() {
            public double xctr() { return x1sm/pts.length; }
            public double yctr() { return y1sm/pts.length; }
        };
    }

    @Override
    public double rad2() {
        double d = 0;
        double xc = x1sm/pts.length;
        double yc = y1sm/pts.length;
        for(Point2D p: pts) {
            double dc=p.dmin2(xc, yc);
            if(d<dc) d=dc;
        }
        return d;
    }

    @Override
    public double msr() {
        if(pts.length<=2) return 0;
        Point2D p0 = pts[0];
        Point2D p1 = pts[1];
        double a = 0;
        for(int i=2; i<pts.length; i++) {
            Point2D p2 = pts[i];
            a += area(p0,p1,p2);
            p1 = p2;
        }
        return a<0? -a:a;
    }

    @Override
    public boolean contains(double x, double y) {
        return (winding(x, y, pts)&1)!=0;
    }

    @Override
    public double dmin2(double x, double y) {
        if(contains(x,y)) return 0;
        return distmin2(x, y, pts);
    }

    @Override
    public double dmax2(double x, double y) {
        return distmax2(x, y, pts);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('[').append(' ');
        boolean first = true;
        for(Point2D p: pts) {
            if(first) first = false;
            else buf.append(" - ");
            buf.append(p.toString());
        }
        buf.append(' ').append(']');
        return buf.toString();
    }

    public static int winding(double x, double y, Point2D[] pts) {
        if(pts.length<2) return 0;
        // MacMartin's algorithm
        int nc = 0;
        double x0 = pts[pts.length-1].xctr();
        double y0 = pts[pts.length-1].yctr();
        for(Point2D p: pts) {
            double xi=p.xctr();
            double yi=p.yctr();
            if(y0<=y&&yi>y) {
                double dx=xi-x0;
                double dy=yi-y0;
                if(((x0-x)*dy-(y0-y)*dx)*dy>0) nc++;
            }
            else if(y0>=y&&yi<y) {
                double dx=xi-x0;
                double dy=yi-y0;
                if(((x0-x)*dy-(y0-y)*dx)*dy>0) nc--;
            }
            x0=xi;
            y0=yi;
        }
        return nc;
    }

    public static double distmin2(double x, double y, Point2D[] pts) {
        if(pts.length<1) return Double.MAX_VALUE;
        double x0 = pts[pts.length-1].xctr()-x;
        double y0 = pts[pts.length-1].yctr()-y;
        double d0 = x0*x0+y0*y0;
        double d = d0;
        for(Point2D p: pts) {
            double xi=p.xctr()-x;
            double yi=p.yctr()-y;
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
            x0=xi;
            y0=yi;
            d0=di;
        }
        return d;
    }

    public static double distmax2(double x, double y, Point2D[] pts) {
        double d = 0;
        for(Point2D p: pts) {
            double xi=p.xctr()-x;
            double yi=p.yctr()-y;
            double di=xi*xi+yi*yi;
            if(d<di) d=di;
        }
        return d;
    }

    public static double area(Point2D p0, Point2D p1, Point2D p2) {
        double x0 = p0.xctr();
        double y0 = p0.yctr();
        double x1 = p1.xctr();
        double y1 = p1.yctr();
        double x2 = p2.xctr();
        double y2 = p2.yctr();
        double dx0 = x1-x0;
        double dy0 = y1-y0;
        double dx1 = x2-x0;
        double dy1 = y2-y0;
        double dx2 = x2-x1;
        double dy2 = y2-y1;
        double d0 = dx0*dx0+dy0*dy0;
        double d1 = dx1*dx1+dy1*dy1;
        double d2 = dx2*dx2+dy2*dy2;
        // Derived from Heron's formula
//        double s = d0+d1+d2;
//        double a = s*s - 2 * (d0*d0+d1*d1+d2*d2);
        // Careful of numerical drift here... it's easy to get a<0 with the wrong sequence like above
        double a = 2*d0*d1 + 2*d1*d2 + 2*d2*d0 - d0*d0 - d1*d1 - d2*d2;
        double d = 0.25 * Math.sqrt(a);
        double z = dx0*dy1-dx1*dy0;
        if(Double.isNaN(d)) System.err.println("Poly area ??? d="+d+" a="+a+" z="+z);
        return (z>=0) ? d:-d;
    }

    public static double angle(Point2D p0, Point2D p1, Point2D p2) {
        double x0 = p0.xctr();
        double y0 = p0.yctr();
        double x1 = p1.xctr();
        double y1 = p1.yctr();
        double x2 = p2.xctr();
        double y2 = p2.yctr();
        double dx0 = x1-x0;
        double dy0 = y1-y0;
        double dx1 = x2-x0;
        double dy1 = y2-y0;
        double d1 = dx0*dx0 + dy0*dy0;
        double d2 = dx1*dx1 + dy1*dy1;
        double dp = dx0*dx1 + dy0*dy1;
        double a = Math.acos(dp/Math.sqrt(d1 * d2));
        double z = dx0*dy1-dx1*dy0;
        return (z>=0) ? a:-a;
    }

    @Override
    public Iterator<Point2D> iterator() {
        return new Iterator<Point2D>() {
            private volatile int pos=0;
            @Override
            public boolean hasNext() { return pos<pts.length; }
            @Override
            public Point2D next() { return pts[pos++]; }
            @Override
            public void remove() { }
        };
    }

}
