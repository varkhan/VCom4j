package net.varkhan.core.geo.shape.rec;


import net.varkhan.core.geo.shape.Point;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:36 PM
 */
public class PointD extends AbstractPoint {
    protected double[] coords;

    public PointD(double... coords) { this.coords=coords.clone(); }
    public PointD(Point point) { this.coords=point.cctr(); }

    public int dim() { return coords.length; }
    public double cctr(int d) { return d<coords.length?coords[d]:0; }

    public double[] cctr() { return coords.clone(); }

    public boolean contains(double... point) {
        int i=0;
        while(i<point.length && i<coords.length) {
            if(point[i]!=coords[i]) return false;
            i++;
        }
        while(i<point.length) {
            if(point[i]!=0)return false;
            i++;
        }
        while(i<coords.length) {
            if(coords[i]!=0) return false;
            i++;
        }
        return true;
    }

    public double dmin2(double... point) {
        double dist2 = 0;
        int i=0;
        while(i<point.length && i<coords.length) {
            final double delta = point[i]-coords[i];
            dist2 += delta*delta;
            i++;
        }
        while(i<point.length) {
            final double delta = point[i];
            dist2 += delta*delta;
            i++;
        }
        while(i<coords.length) {
            final double delta = coords[i];
            dist2 += delta*delta;
            i++;
        }
        return dist2;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof PointD)) return false;
        Point that=(Point) o;
        int dim = coords.length;
        if(dim!=that.dim()) return false;
        for (int i=0;i<dim;i++) if(coords[i]!=that.cctr(i)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return hashCode(coords);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toString(buf, coords);
        return buf.toString();
    }

}
