package net.varkhan.core.geo.shape.d3.rec;

import net.varkhan.core.geo.shape.d3.Point3D;
import net.varkhan.core.geo.shape.d3.Rect3D;
import net.varkhan.core.geo.shape.d3.Shape3D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 3:31 PM
 */
public class RectF3D extends AbstractShape3D implements Rect3D {

    protected float xmin;
    protected float xmax;
    protected float ymin;
    protected float ymax;
    protected float zmin;
    protected float zmax;

    public RectF3D(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax) {
        this.xmin=xmin;
        this.xmax=xmax;
        this.ymin=ymin;
        this.ymax=ymax;
        this.zmin=zmin;
        this.zmax=zmax;
    }

    public RectF3D(Shape3D shape) {
        this((float)shape.xmin(),(float)shape.xmax(),(float)shape.ymin(),(float)shape.ymax(),(float)shape.zmin(),(float)shape.zmax());
    }

    public RectF3D(Point3D pmin, Point3D pmax) {
        this((float)pmin.xctr(),(float)pmax.xctr(),(float)pmin.yctr(),(float)pmax.yctr(),(float)pmin.zctr(),(float)pmax.zctr());
    }

    public RectF3D(Point3D point, float xdel, float ydel, float zdel) {
        this((float)point.xctr()-xdel,(float)point.xctr()+xdel,(float)point.yctr()-ydel,(float)point.yctr()+ydel,(float)point.zctr()-zdel,(float)point.zctr()+zdel);
    }

    public double xmin() { return xmin; }
    public double xmax() { return xmax; }
    public double ymin() { return ymin; }
    public double ymax() { return ymax; }
    public double zmin() { return zmin; }
    public double zmax() { return zmax; }

    @Override
    public double xctr() {
        return 0.5*(xmin+xmax);
    }

    @Override
    public double yctr() {
        return 0.5*(ymin+ymax);
    }


    @Override
    public double zctr() {
        return 0.5*(zmin+zmax);
    }

    @Override
    public double rad2() {
        double deltax = xmax-xmin;
        double deltay = ymax-ymin;
        return 0.25*(deltax*deltax+deltay*deltay);
    }

    @Override
    public double length() { return Double.POSITIVE_INFINITY; }

    @Override
    public double area() {
        double deltax = xmax-xmin;
        double deltay = ymax-ymin;
        double deltaz = zmax-zmin;
        return 2*(deltax*deltay+deltax*deltaz+deltay*deltaz);
    }

    @Override
    public double volume() {
        double deltax = xmax-xmin;
        double deltay = ymax-ymin;
        return deltax*deltay;
    }

    @Override
    public double dmin2(double x, double y, double z) {
        double deltax = 0;
        if(xmin>x) deltax = xmin-x;
        if(x>xmax) deltax = x-xmax;
        double deltay = 0;
        if(ymin>y) deltay = ymin-y;
        if(y>ymax) deltay = y-ymax;
        double deltaz = 0;
        if(zmin>z) deltaz = zmin-z;
        if(z>zmax) deltaz = z-zmax;
        return deltax*deltax+deltay*deltay+deltaz*deltaz;
    }

    @Override
    public double dmax2(double x, double y, double z) {
        double deltax = 0;
        double dxmin = xmin-x;
        if(deltax<+dxmin) deltax = +dxmin;
        if(deltax<-dxmin) deltax = -dxmin;
        double dxmax = x-xmax;
        if(deltax<+dxmax) deltax = +dxmax;
        if(deltax<-dxmax) deltax = -dxmax;
        double deltay = 0;
        double dymin = ymin-y;
        if(deltay<+dymin) deltay = +dymin;
        if(deltay<-dymin) deltay = -dymin;
        double dymax = y-ymax;
        if(deltay<+dymax) deltay = +dymax;
        if(deltay<-dymax) deltay = -dymax;
        double deltaz = 0;
        double dzmin = zmin-z;
        if(deltaz<+dzmin) deltaz = +dzmin;
        if(deltaz<-dzmin) deltaz = -dzmin;
        double dzmax = z-zmax;
        if(deltaz<+dzmax) deltaz = +dzmax;
        if(deltaz<-dzmax) deltaz = -dzmax;
        return deltax*deltax+deltay*deltay+deltaz*deltaz;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof Rect3D){
            Rect3D that=(Rect3D) o;
            return this.xmin==that.xmin() && this.xmax==that.xmax()
                   && this.ymin==that.ymin() && this.ymax==that.ymax()
                   && this.zmin==that.zmin() && this.ymax==that.zmax();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(xmin) + 31*Float.floatToIntBits(xmax)
               + 61*Float.floatToIntBits(ymin) + 89*Float.floatToIntBits(ymax)
               + 127*Float.floatToIntBits(zmin) + 151*Float.floatToIntBits(zmax);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ');
        buf.append('[').append(xmin).append(':').append(xmax).append(']');
        buf.append(" x ");
        buf.append('[').append(ymin).append(':').append(ymax).append(']');
        buf.append(" x ");
        buf.append('[').append(zmin).append(':').append(zmax).append(']');
        buf.append(' ').append(')');
        return buf.toString();
    }

}
