package net.varkhan.core.geo.shape.d2.rec;

import net.varkhan.core.geo.shape.d2.Point2D;
import net.varkhan.core.geo.shape.d2.Shape2D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 2:21 PM
 */
public class CircleD2D extends AbstractShape2D implements Shape2D {
    protected double x;
    protected double y;
    protected double r;

    public CircleD2D(double x, double y, double r) {
        this.x=x;
        this.y=y;
        this.r=r<0?0:r;
    }

    public CircleD2D(Point2D center, double r) {
        this(center.xctr(),center.yctr(),r);
    }

    public double xmin() { return x-r; }
    public double xmax() { return x+r; }
    public double ymin() { return y-r; }
    public double ymax() { return y+r; }

    public double xctr() { return x; }
    public double yctr() { return y; }

    public Point2D ctr() {
        return new AbstractPoint2D() {
            public double xctr() { return x; }
            public double yctr() { return y; }
        };
    }

    public double rad() { return r; }
    public double rad2() { return r*r; }
    public double length() { return 2*Math.PI*r; }
    public double area() { return Math.PI*r*r; }

    public boolean contains(double x, double y) {
        return dctr2(x, y)<r*r;
    }

    private double dctr2(double x, double y) {
        double dx = this.x-x;
        double dy = this.y-y;
        return dx*dx+dy*dy;
    }

    public double dmin(double x, double y) {
        double d = Math.sqrt(dctr2(x, y));
        return d<r?0:d-r;
    }

    @Override
    public double dmax(double x, double y) {
        return Math.sqrt(dctr2(x, y))+r;
    }

    @Override
    public double dmin2(double x, double y) {
        double d2 = dctr2(x, y);
        return d2<r*r?0:d2+r*(r-2*Math.sqrt(d2));
    }

    @Override
    public double dmax2(double x, double y) {
        double d2 = dctr2(x, y);
        return d2+r*(2*Math.sqrt(d2)+r);
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof CircleD2D){
            CircleD2D that=(CircleD2D) o;
            return this.x==that.xctr() && this.y==that.yctr() && this.r==that.rad();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits((float) x) + 31*Float.floatToIntBits((float)y)
               + 61*Float.floatToIntBits((float)r);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ');
        buf.append('(').append(x).append(' ').append(y).append(')');
        buf.append(" @ ").append(r);
        buf.append(' ').append(')');
        return buf.toString();
    }

}
