package net.varkhan.core.geo.shape.d3.rec;

import net.varkhan.core.geo.shape.rec.AbstractShape;
import net.varkhan.core.geo.shape.d3.Point3D;
import net.varkhan.core.geo.shape.d3.Shape3D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 3:12 PM
 */
public abstract class AbstractShape3D extends AbstractShape implements Shape3D {

    public int dim() { return 3; }

    public double cmin(int d) {
        switch(d) {
            case 0: return xmin();
            case 1: return ymin();
            case 2: return zmin();
            default: return 0;
        }
    }
    public double cmax(int d) {
        switch(d) {
            case 0: return xmax();
            case 1: return ymax();
            case 2: return zmax();
            default: return 0;
        }
    }
    public double[] cmin() { return new double[] { xmin(), ymin(), zmin() }; }
    public double[] cmax() { return new double[] { xmax(), ymax(), zmax() }; }

    public double cctr(int d) {
        switch(d) {
            case 0: return xctr();
            case 1: return yctr();
            case 2: return zctr();
            default: return 0;
        }
    }
    public double[] cctr() { return new double[] { xctr(), yctr(), zctr() }; }
    public Point3D ctr() { return new AbstractPoint3D() {
        public double xctr() { return AbstractShape3D.this.xctr(); }
        public double yctr() { return AbstractShape3D.this.yctr(); }
        public double zctr() { return AbstractShape3D.this.zctr(); }
    }; }

    @Override
    public double msr() { return volume(); }

    @Override
    public double msr(int d) {
        switch(d) {
            case 0: return Double.POSITIVE_INFINITY;
            case 1: return length();
            case 2: return area();
            case 3: return volume();
        }
        return 0;
    }


    @Override
    public double dmin2(double... point) {
        double x = point.length>=1?point[0]:0;
        double y = point.length>=2?point[1]:0;
        double z = point.length>=3?point[2]:0;
        double dist2 = dmin2(x, y, z);
        int i=3;
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
        double z = point.length>=3?point[2]:0;
        double dist2 = dmax2(x, y, z);
        int i=3;
        while(i<point.length) {
            double delta = point[i];
            dist2 += delta*delta;
            i ++;
        }
        return dist2;
    }

    public boolean contains(double x, double y, double z) { return dmin2(x, y, z)==0; }

    public double dmax(double x, double y, double z) { return Math.sqrt(dmax2(x, y, z)); }
    public double dmin(double x, double y, double z) { return Math.sqrt(dmin2(x, y, z)); }

}
