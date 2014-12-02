package net.varkhan.core.geo.shape.d2.rec;

import net.varkhan.core.geo.shape.d2.Point2D;
import net.varkhan.core.geo.shape.d2.Rect2D;
import net.varkhan.core.geo.shape.d2.Shape2D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 3:31 PM
 */
public class RectF2D extends AbstractShape2D implements Rect2D {

    protected float xmin;
    protected float xmax;
    protected float ymin;
    protected float ymax;

    public RectF2D(float xmin, float xmax, float ymin, float ymax) {
        this.xmin=xmin;
        this.xmax=xmax;
        this.ymin=ymin;
        this.ymax=ymax;
    }

    public RectF2D(Shape2D shape) {
        this((float)shape.xmin(),(float)shape.xmax(),(float)shape.ymin(),(float)shape.ymax());
    }

    public RectF2D(Point2D point, float xdel, float ydel) {
        this((float)point.xctr()-xdel,(float)point.xctr()+xdel,(float)point.yctr()-ydel,(float)point.yctr()+ydel);
    }

    public double xmin() { return xmin; }
    public double xmax() { return xmax; }
    public double ymin() { return ymin; }
    public double ymax() { return ymax; }

    @Override
    public double xctr() {
        return 0.5*(xmin+xmax);
    }

    @Override
    public double yctr() {
        return 0.5*(ymin+ymax);
    }

    @Override
    public double rad2() {
        double deltax = xmax-xmin;
        double deltay = ymax-ymin;
        return 0.25*(deltax*deltax+deltay*deltay);
    }

    @Override
    public double msr() {
        double deltax = xmax-xmin;
        double deltay = ymax-ymin;
        return deltax*deltay;
    }

    public boolean contains(double x, double y) {
        return xmin<=x && x<=xmax && ymin<=y && y<=ymax;
    }

    @Override
    public double dmin2(double x, double y) {
        double deltax = 0;
        if(xmin>x) deltax = xmin-x;
        if(x>xmax) deltax = x-xmax;
        double deltay = 0;
        if(ymin>y) deltay = ymin-y;
        if(y>ymax) deltay = y-ymax;
        return deltax*deltax+deltay*deltay;
    }

    @Override
    public double dmax2(double x, double y) {
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
        return deltax*deltax+deltay*deltay;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof Rect2D){
            Rect2D that=(Rect2D) o;
            return this.xmin==that.xmin() && this.xmax==that.xmax()
                   && this.ymin==that.xmin() && this.ymax==that.ymax();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(xmin) + 31*Float.floatToIntBits(xmax)
               + 61*Float.floatToIntBits(ymin) + 89*Float.floatToIntBits(ymax);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ');
        buf.append('[').append(xmin).append(':').append(xmax).append(']');
        buf.append(" x ");
        buf.append('[').append(ymin).append(':').append(ymax).append(']');
        buf.append(' ').append(')');
        return buf.toString();
    }

}
