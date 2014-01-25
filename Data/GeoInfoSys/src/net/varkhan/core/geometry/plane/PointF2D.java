package net.varkhan.core.geometry.plane;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 2:02 PM
 */
public class PointF2D extends AbstractPoint2D {
    protected float x;
    protected float y;

    public PointF2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF2D(double x, double y) {
        this((float)x,(float)y);
    }

    public PointF2D(Point2D point) {
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
        return Float.floatToIntBits(x)+31*Float.floatToIntBits(y);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ').append(x).append(' ').append(y).append(' ').append(')');
        return buf.toString();
    }

}
