package net.varkhan.core.geo.geometry.d3.rec;

import net.varkhan.core.geo.geometry.d3.Point3D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 2:02 PM
 */
public class PointD3D extends AbstractPoint3D {
    protected double x;
    protected double y;
    protected double z;

    public PointD3D(double x, double y, double z) {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public PointD3D(Point3D point) {
        this(point.xctr(),point.yctr(),point.zctr());
    }

    public double xctr() { return x; }
    public double yctr() { return y; }
    public double zctr() { return z; }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof Point3D) {
            Point3D that=(Point3D) o;
            return this.x==that.xctr() && this.y==that.yctr() && this.z==that.zctr();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits((float)x)+31*Float.floatToIntBits((float)y)+61*Float.floatToIntBits((float)z);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ').append(x).append(' ').append(y).append(' ').append(z).append(' ').append(')');
        return buf.toString();
    }

}
