package net.varkhan.core.geo.shape.d2.rec;

import net.varkhan.core.geo.shape.d2.Point2D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 2:02 PM
 */
public class PointD2D extends AbstractPoint2D {
    protected double x;
    protected double y;

    public PointD2D(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public PointD2D(Point2D point) {
        this(point.xctr(),point.yctr());
    }

    public double xctr() { return x; }
    public double yctr() { return y; }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof Point2D) {
            Point2D that=(Point2D) o;
            return this.x==that.xctr() && this.y==that.yctr();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashCode(x, y);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toString(buf, x, y);
        return buf.toString();
    }

}
