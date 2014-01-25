package net.varkhan.core.geometry.plane;

import net.varkhan.core.geometry.AbstractShape;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 3:12 PM
 */
public abstract class AbstractShape2D extends AbstractShape implements Shape2D {

    public int dim() { return 2; }

    public double cmin(int d) {
        switch(d) {
            case 0: return xmin();
            case 1: return ymin();
            default: return 0;
        }
    }
    public double cmax(int d) {
        switch(d) {
            case 0: return xmax();
            case 1: return ymax();
            default: return 0;
        }
    }
    public double[] cmin() { return new double[] { xmin(), ymin() }; }
    public double[] cmax() { return new double[] { xmax(), ymax() }; }

    public double cctr(int d) {
        switch(d) {
            case 0: return xctr();
            case 1: return yctr();
            default: return 0;
        }
    }
    public double[] cctr() { return new double[] { xctr(), yctr() }; }
    public Point2D ctr() { return new AbstractPoint2D() {
        public double xctr() { return AbstractShape2D.this.xctr(); }
        public double yctr() { return AbstractShape2D.this.yctr(); }
    }; }


    @Override
    public double dmin2(double... point) {
        double x = point.length>=1?point[0]:0;
        double y = point.length>=2?point[1]:0;
        double dist2 = dmin2(x, y);
        int i=2;
        while(i<point.length) {
            double delta = point[i];
            dist2 += delta*delta;
            i ++;
        }
        return dist2;
    }

    @Override
    public double dmax2(double... point) {
        double x = point.length>=1?point[0]:0;
        double y = point.length>=2?point[1]:0;
        double dist2 = dmax2(x, y);
        int i=2;
        while(i<point.length) {
            double delta = point[i];
            dist2 += delta*delta;
            i ++;
        }
        return dist2;
    }

    public boolean contains(double x, double y) { return dmin2(x, y)==0; }

    public double dmax(double x, double y) { return Math.sqrt(dmax2(x, y)); }
    public double dmin(double x, double y) { return Math.sqrt(dmin2(x, y)); }

}
