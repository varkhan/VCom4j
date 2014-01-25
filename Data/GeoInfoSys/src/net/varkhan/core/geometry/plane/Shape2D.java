package net.varkhan.core.geometry.plane;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:07 PM
 */
public interface Shape2D {

    public double xmin();
    public double xmax();
    public double ymin();
    public double ymax();

    public double xctr();
    public double yctr();
    public Point2D ctr();
    public double rad();
    public double rad2();
    public double msr();

    public boolean contains(double x, double y);
    public double dmin(double x, double y);
    public double dmax(double x, double y);
    public double dmin2(double x, double y);
    public double dmax2(double x, double y);

}
