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

    /** The number of dimensions this shape is defined in */
    public int dim();

    /** The minimum coordinate attained by the shape on a particular dimension d */
    public double cmin(int d);
    /** The maximum coordinate attained by the shape on a particular dimension d */
    public double cmax(int d);
    /** A vector of minimum coordinates attained by the shape (lower coordinates of the bounding box) */
    public double[] cmin();
    /** A vector of maximum coordinates attained by the shape (higher coordinates of the bounding box) */
    public double[] cmax();

    /** The coordinate of the shape's center of gravity on a particular dimension d */
    public double cctr(int d);
    /** A vector of the shape's center of gravity coordinate */
    public double[] cctr();
    /** A Point representing the shape's center of gravity */
    public Point ctr();
    /** The radius of the shape's bounding spheroid (maximum attained distance of a shape's point from the center of gravity) */
    public double rad();
    /** The square radius of the shape's bounding spheroid (maximum attained distance of a shape's point from the center of gravity) */
    public double rad2();
    /** The shape's measure in its natural dimensional space (1-dimensional length, 2-dimensional area, 3-dimensional volume, etc...) */
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
