package net.varkhan.core.geo.shape.d3.rec;

import net.varkhan.core.geo.shape.d3.Point3D;
import net.varkhan.core.geo.shape.d3.Shape3D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 2:21 PM
 */
public class SphereD3D extends AbstractShape3D implements Shape3D {
    protected static final double VOL_FACTOR=(4/3)*Math.PI;
    protected static final double AREA_FACTOR=4*Math.PI;
    protected double x;
    protected double y;
    protected double z;
    protected double r;

    public SphereD3D(double x, double y, double z, double r) {
        this.x=x;
        this.y=y;
        this.z=z;
        this.r=r<0 ? 0 : r;
    }

    public SphereD3D(Point3D center, double r) {
        this(center.xctr(), center.yctr(), center.zctr(), r);
    }

    public double xmin() { return x-r; }
    public double xmax() { return x+r; }
    public double ymin() { return y-r; }
    public double ymax() { return y+r; }
    public double zmin() { return z-r; }
    public double zmax() { return z+r; }

    public double xctr() { return x; }
    public double yctr() { return y; }
    public double zctr() { return z; }

    public Point3D ctr() {
        return new AbstractPoint3D() {
            public double xctr() { return x; }
            public double yctr() { return y; }
            public double zctr() { return z; }
        };
    }

    public double rad() { return r; }
    public double rad2() { return r*r; }
    public double length() { return Double.POSITIVE_INFINITY; }
    public double area() { return AREA_FACTOR*r*r; }
    public double volume() { return VOL_FACTOR*r*r*r; }

    public boolean contains(double x, double y, double z) {
        return dctr2(x, y, z)<r*r;
    }

    private double dctr2(double x, double y, double z) {
        double dx = this.x-x;
        double dy = this.y-y;
        double dz = this.z-z;
        return dx*dx+dy*dy+dz*dz;
    }

    public double dmin(double x, double y, double z) {
        double d = Math.sqrt(dctr2(x, y, z));
        return d<r?0:d-r;
    }

    @Override
    public double dmax(double x, double y, double z) {
        return Math.sqrt(dctr2(x, y, z))+r;
    }

    @Override
    public double dmin2(double x, double y, double z) {
        double d2 = dctr2(x, y, z);
        return d2<r*r?0:d2+r*(r-2*Math.sqrt(d2));
    }

    @Override
    public double dmax2(double x, double y, double z) {
        double d2 = dctr2(x, y, z);
        return d2+r*(2*Math.sqrt(d2)+r);
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof SphereD3D){
            SphereD3D that=(SphereD3D) o;
            return this.x==that.xctr() && this.y==that.yctr() && this.z==that.zctr() && this.r==that.rad();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits((float) x) + 31*Float.floatToIntBits((float)y)
               + 61*Float.floatToIntBits((float)z) + 89*Float.floatToIntBits((float)r);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ');
        buf.append('(').append(x).append(' ').append(y).append(' ').append(z).append(')');
        buf.append(" @ ").append(r);
        buf.append(' ').append(')');
        return buf.toString();
    }

}
