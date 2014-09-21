package net.varkhan.core.geo.geometry;

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

}
