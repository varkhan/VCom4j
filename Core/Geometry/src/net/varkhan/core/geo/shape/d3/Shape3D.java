package net.varkhan.core.geo.shape.d3;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:07 PM
 */
public interface Shape3D {

    public double xmin();
    public double xmax();
    public double ymin();
    public double ymax();
    public double zmin();
    public double zmax();

    public double xctr();
    public double yctr();
    public double zctr();
    public Point3D ctr();
    public double rad();
    public double rad2();
    public double msr();

    public boolean contains(double x, double y, double z);
    public double dmin(double x, double y, double z);
    public double dmax(double x, double y, double z);
    public double dmin2(double x, double y, double z);
    public double dmax2(double x, double y, double z);

}
