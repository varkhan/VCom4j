package net.varkhan.core.geo.geometry.d2.rec;


import net.varkhan.core.geo.geometry.d2.Point2D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:49 PM
 */
public abstract class AbstractPoint2D extends AbstractShape2D implements Point2D {

    public int dim() { return 2; }
    public double xmin() { return xctr(); }
    public double xmax() { return xctr(); }
    public double ymin() { return yctr(); }
    public double ymax() { return yctr(); }

    public double cctr(int d) {
        switch(d) {
            case 0: return xctr();
            case 1: return yctr();
            default: return 0;
        }
    }
    public double[] cctr() { return new double[] { xctr(), yctr() }; }
    public Point2D ctr() { return this; }
    public double rad() { return 0; }
    public double rad2() { return 0; }
    public double msr() { return 0; }

    public boolean contains(double x, double y) { return x==xctr() && y==yctr(); }
    public double dmin(double x, double y) { return Math.sqrt(dmin2(x, y)); }
    public double dmax(double x, double y) { return dmin(x, y); }
    public double dmax2(double x, double y) { return dmin2(x, y); }

    public double dmin2(double x, double y) {
        double deltaX = x-xctr();
        double deltaY = y-yctr();
        return deltaX*deltaX + deltaY*deltaY;
    }

    public double dmin2(double... point) {
        double dist2 = 0;
        int i=0;
        if(i<point.length) {
            double delta = point[i]-xctr();
            dist2 += delta*delta;
            i ++;
        }
        if(i<point.length) {
            double delta = point[i]-yctr();
            dist2 += delta*delta;
            i ++;
        }
        while(i<point.length) {
            double delta = point[i];
            dist2 += delta*delta;
            i ++;
        }
        return dist2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("Point");
        buf.append('(').append(xctr()).append(',').append(yctr()).append(')');
        return buf.toString();
    }
}
