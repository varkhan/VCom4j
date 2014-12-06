package net.varkhan.core.geo.shape.rec;

import net.varkhan.core.geo.shape.Point;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:46 PM
 */
public abstract class AbstractPoint extends AbstractShape implements Point {

    public int dim() { return cctr().length; }

    public double cmin(int d) { return cctr(d); }
    public double cmax(int d) { return cctr(d); }
    public double[] cmin() { return cctr(); }
    public double[] cmax() { return cctr(); }

    public Point ctr() { return this; }
    public double rad() { return 0; }
    public double rad2() { return 0; }
    public double msr() { return 0; }
    public double msr(int d) { return d<=0 ? Double.POSITIVE_INFINITY : 0; }

    public double dmax2(double... point) { return dmin2(point); }
    public double dmin2(double... point) {
        final double[] coords = cctr();
        double dist2 = 0;
        int i=0;
        while(i<point.length && i<coords.length) {
            final double delta = point[i]-coords[i];
            dist2 += delta*delta;
            i++;
        }
        while(i<point.length) {
            final double delta = point[i];
            dist2 += delta*delta;
            i++;
        }
        while(i<coords.length) {
            final double delta = coords[i];
            dist2 += delta*delta;
            i++;
        }
        return dist2;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toString(buf, cctr());
        return buf.toString();
    }

    public static int hashCode(double... coords) {
        int h = 0;
        for(int i=0;i<coords.length;i++) {
            long bits=Double.doubleToRawLongBits(coords[i]);
            h=31*h+(int) (bits^(bits>>>32));
        }
        return h;
    }

    public static void toString(StringBuilder buf, double... coords) {
        buf.append('(').append(' ');
        for(double c: coords) { buf.append(c).append(' '); }
        buf.append(')');
    }

    public static int hashCode(float... coords) {
        int h = 0;
        for(int i=0;i<coords.length;i++) h=31*h+Float.floatToRawIntBits(coords[i]);
        return h;
    }

    public static void toString(StringBuilder buf, float... coords) {
        buf.append('(').append(' ');
        for(float c: coords) { buf.append(c).append(' '); }
        buf.append(')');
    }

}
