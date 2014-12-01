package net.varkhan.core.geo.geometry.d3.rec;


import net.varkhan.core.geo.geometry.d3.Point3D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:49 PM
 */
public abstract class AbstractPoint3D extends AbstractShape3D implements Point3D {

    public int dim() { return 3; }
    public double xmin() { return xctr(); }
    public double xmax() { return xctr(); }
    public double ymin() { return yctr(); }
    public double ymax() { return yctr(); }
    public double zmin() { return zctr(); }
    public double zmax() { return zctr(); }

    public double cctr(int d) {
        switch(d) {
            case 0: return xctr();
            case 1: return yctr();
            case 2: return zctr();
            default: return 0;
        }
    }
    public double[] cctr() { return new double[] { xctr(), yctr(), zctr() }; }
    public Point3D ctr() { return this; }
    public double rad() { return 0; }
    public double rad2() { return 0; }
    public double msr() { return 0; }

    public boolean contains(double x, double y, double z) { return x==xctr() && y==yctr() && z==zctr(); }
    public double dmin(double x, double y, double z) { return Math.sqrt(dmin2(x, y, z)); }
    public double dmax(double x, double y, double z) { return dmin(x, y, z); }
    public double dmax2(double x, double y, double z) { return dmin2(x, y, z); }

    public double dmin2(double x, double y, double z) {
        double deltaX = x-xctr();
        double deltaY = y-yctr();
        double deltaZ = z-zctr();
        return deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ;
    }

    public double dmin2(double... point) {
        double dist2 = 0;
        int i=0;
        if(i<point.length) {
            double delta = point[i]-xctr();
            dist2 += delta*delta;
            i ++;
        }
        if(i<point.length) {
            double delta = point[i]-yctr();
            dist2 += delta*delta;
            i ++;
        }
        if(i<point.length) {
            double delta = point[i]-zctr();
            dist2 += delta*delta;
            i ++;
        }
        while(i<point.length) {
            double delta = point[i];
            dist2 += delta*delta;
            i ++;
        }
        return dist2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("Point");
        buf.append('(').append(xctr()).append(',').append(yctr()).append(',').append(zctr()).append(')');
        return buf.toString();
    }
}
