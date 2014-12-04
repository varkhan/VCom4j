package net.varkhan.core.geo.shape.d2.rec;

import net.varkhan.core.geo.shape.d2.Point2D;
import net.varkhan.core.geo.shape.d2.Shape2D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/16/14
 * @time 9:05 PM
 */
public class WrapShape2D extends AbstractShape2D {
    protected Shape2D shape;

    public WrapShape2D(Shape2D shape) {
        this.shape=shape;
    }

    @Override
    public double xmin() {return shape.xmin();}

    @Override
    public double xmax() {return shape.xmax();}

    @Override
    public double ymin() {return shape.ymin();}

    @Override
    public double ymax() {return shape.ymax();}

    @Override
    public double xctr() {return shape.xctr();}

    @Override
    public double yctr() {return shape.yctr();}

    @Override
    public Point2D ctr() {return shape.ctr();}

    @Override
    public double rad() {return shape.rad();}

    @Override
    public double rad2() {return shape.rad2();}

    @Override
    public double length() {return shape.length();}

    @Override
    public double area() {return shape.area();}

    @Override
    public boolean contains(double x, double y) {return shape.contains(x, y);}

    @Override
    public double dmin(double x, double y) {return shape.dmin(x, y);}

    @Override
    public double dmax(double x, double y) {return shape.dmax(x, y);}

    @Override
    public double dmin2(double x, double y) {return shape.dmin2(x, y);}

    @Override
    public double dmax2(double x, double y) {return shape.dmax2(x, y);}

    @Override
    public boolean contains(Point2D point) {return shape.contains(point);}

    @Override
    public double dmin(Point2D point) {return shape.dmin(point);}

    @Override
    public double dmax(Point2D point) {return shape.dmax(point);}

    @Override
    public double dmin2(Point2D point) {return shape.dmin2(point);}

    @Override
    public double dmax2(Point2D point) {return shape.dmax2(point);}

    @Override
    public String toString() {
        return shape.toString();
    }

}
