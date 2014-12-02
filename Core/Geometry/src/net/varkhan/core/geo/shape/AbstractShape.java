package net.varkhan.core.geo.shape;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:25 PM
 */
public abstract class AbstractShape implements Shape {

    public double cmin(int d) { return d<dim() ? cmin()[d] : 0; }
    public double cmax(int d) { return d<dim() ? cmax()[d] : 0; }

    public double cctr(int d) { return d<dim() ? cctr()[d] : 0; }

    public Point ctr() { return new AbstractPoint() {
        public double[] cctr() { return AbstractShape.this.cctr(); }
    }; }

    public double rad() { return Math.sqrt(rad2()); }
    public boolean contains(double... point) { return dmin2(point)==0; }
    public double dmin(double... point) { return Math.sqrt(dmin2(point)); }
    public double dmax(double... point) { return Math.sqrt(dmax2(point)); }

    public boolean contains(Point point) { return contains(point.cctr()); }
    public double dmin(Point point) { return dmin(point.cctr()); }
    public double dmax(Point point) { return dmax(point.cctr()); }
    public double dmin2(Point point) { return dmin2(point.cctr()); }
    public double dmax2(Point point) { return dmax2(point.cctr()); }

}
