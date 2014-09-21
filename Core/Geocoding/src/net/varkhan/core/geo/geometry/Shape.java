package net.varkhan.core.geo.geometry;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:07 PM
 */
public interface Shape {

    public int dim();

    public double cmin(int d);
    public double cmax(int d);
    public double[] cmin();
    public double[] cmax();

    public double cctr(int d);
    public double[] cctr();
    public Point ctr();
    public double rad();
    public double rad2();
    public double msr();

    public boolean contains(double... point);
    public double dmin(double... point);
    public double dmax(double... point);
    public double dmin2(double... point);
    public double dmax2(double... point);
    public boolean contains(Point point);
    public double dmin(Point point);
    public double dmax(Point point);
    public double dmin2(Point point);
    public double dmax2(Point point);

}
