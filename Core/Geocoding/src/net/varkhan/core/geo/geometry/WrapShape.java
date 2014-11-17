package net.varkhan.core.geo.geometry;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/16/14
 * @time 9:07 PM
 */
public class WrapShape extends AbstractShape {
    protected Shape shape;

    public WrapShape(Shape shape) {
        this.shape=shape;
    }

    @Override
    public int dim() {return shape.dim();}

    @Override
    public double cmin(int d) {return shape.cmin(d);}

    @Override
    public double cmax(int d) {return shape.cmax(d);}

    @Override
    public double[] cmin() {return shape.cmin();}

    @Override
    public double[] cmax() {return shape.cmax();}

    @Override
    public double cctr(int d) {return shape.cctr(d);}

    @Override
    public double[] cctr() {return shape.cctr();}

    @Override
    public Point ctr() {return shape.ctr();}

    @Override
    public double rad() {return shape.rad();}

    @Override
    public double rad2() {return shape.rad2();}

    @Override
    public double msr() {return shape.msr();}

    @Override
    public boolean contains(double... point) {return shape.contains(point);}

    @Override
    public double dmin(double... point) {return shape.dmin(point);}

    @Override
    public double dmax(double... point) {return shape.dmax(point);}

    @Override
    public double dmin2(double... point) {return shape.dmin2(point);}

    @Override
    public double dmax2(double... point) {return shape.dmax2(point);}

    @Override
    public boolean contains(Point point) {return shape.contains(point);}

    @Override
    public double dmin(Point point) {return shape.dmin(point);}

    @Override
    public double dmax(Point point) {return shape.dmax(point);}

    @Override
    public double dmin2(Point point) {return shape.dmin2(point);}

    @Override
    public double dmax2(Point point) {return shape.dmax2(point);}
}
