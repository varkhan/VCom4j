package net.varkhan.core.geo.shape.d2;

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
    public double length();
    public double area();

    public boolean contains(double x, double y);
    public double dmin(double x, double y);
    public double dmax(double x, double y);
    public double dmin2(double x, double y);
    public double dmax2(double x, double y);

    boolean contains(Point2D point);
    double dmin(Point2D point);
    double dmax(Point2D point);
    double dmin2(Point2D point);
    double dmax2(Point2D point);

    /**
     * <b>How to handle "inside" vs. "outside" depending on point order (rotation direction).</b>
     * <li><em>NONE</em> ignores order</li>
     * <li><em>DEX</em> defines "inside" as the right-hand side of the line</li>
     * <li><em>LEV</em> defines "inside" as the left-hand side of the line</li>
     */
    public static enum Chirality {
        NONE,
        DEX,
        LEV,
        ;
    }


}
