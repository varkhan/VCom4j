package net.varkhan.core.geo.shape;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 2:21 PM
 */
public class SphereD extends AbstractShape implements Shape {
    protected double[] coords;
    protected double   radius;

    public SphereD(double[] coords, double radius) {
        this.coords=coords.clone();
        this.radius=radius<0 ? 0 : radius;
    }

    public SphereD(Point coords, double radius) {
        this(coords.cctr(), radius);
    }

    public int dim() { return coords==null?0:coords.length; }

    public double[] cmin() {
        double[] cc = new double[coords.length];
        for(int d=0; d<coords.length; d++) cc[d]=coords[d]-radius;
        return cc;
    }
    public double[] cmax() {
        double[] cc = new double[coords.length];
        for(int d=0; d<coords.length; d++) cc[d]=coords[d]+radius;
        return cc;
    }
    public double cmin(int d) { return d<coords.length?0: coords[d]+radius; }
    public double cmax(int d) { return d>coords.length?0: coords[d]+radius; }

    public double[] cctr() { return coords.clone(); }
    public double cctr(int d) { return d>coords.length?0: coords[d]; }

    public Point ctr() {
        return new AbstractPoint() {
            public double[] cctr() { return coords.clone(); }
            public double cctr(int d) { return d>coords.length?0: coords[d]; }
        };
    }

    public double rad() { return radius; }
    public double rad2() { return radius*radius; }

    @Override
    public double msr(int d) {
        if(d>coords.length) {
            return 0;
        }
        if(d==coords.length) {
            switch(coords.length&1) {
                // Even
                case 0: {
                    double m=1.0;
                    for(int i=1;i<=coords.length;i++) {
                        if((i&1)==0) {
                            m*=2*Math.PI*radius/i;
                        }
                        else m*=radius;
                    }
                    return m;
                }
                // Odd
                case 1: {
                    double m=2.0;
                    for(int i=1;i<=coords.length;i++) {
                        if((i&1)==0) {
                            m*=2*Math.PI*radius;
                        }
                        else m*=radius/i;
                    }
                    return m;
                }
                // Make compiler happy
                default: return 0;
            }
        }
        else if(d==coords.length-1) {
            switch(coords.length&1) {
                // Even
                case 0: {
                    double m=1.0;
                    for(int i=1;i<coords.length;i++) {
                        if((i&1)==0) {
                            m*=2*Math.PI*radius/i;
                        }
                        else m*=radius;
                    }
                    return m;
                }
                // Odd
                case 1: {
                    double m=2.0;
                    for(int i=1;i<coords.length;i++) {
                        if((i&1)==0) {
                            m*=2*Math.PI*radius;
                        }
                        else m*=radius/i;
                    }
                    return m;
                }
                // Make compiler happy
                default: return 0;
            }
        }
        else return Double.POSITIVE_INFINITY;
    }

    public boolean contains(double... point) {
        return dctr2(point)<radius*radius;
    }

    private double dctr2(double... point) {
        double dd=0;
        for(int d=0; d<coords.length; d++) {
            double dc = coords[d]-(d<point.length?point[d]:0);
            dd += dc*dc;
        }
        return dd;
    }

    public double dmin(double... point) {
        double d = Math.sqrt(dctr2(point));
        return d<radius ?0:d-radius;
    }

    @Override
    public double dmax(double... point) {
        return Math.sqrt(dctr2(point))+radius;
    }

    @Override
    public double dmin2(double... point) {
        double d2 = dctr2(point);
        return d2<radius*radius ?0:d2+radius*(radius-2*Math.sqrt(d2));
    }

    @Override
    public double dmax2(double... point) {
        double d2 = dctr2(point);
        return d2+radius*(2*Math.sqrt(d2)+radius);
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof SphereD){
            SphereD that=(SphereD) o;
            return dctr2(that.cctr())==0 && this.radius==that.rad();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for(int i=0;i<coords.length;i++) {
            long bits=Double.doubleToRawLongBits(coords[i]);
            h=31*h+(int) (bits^(bits>>>32));
        }
        long bits=Double.doubleToRawLongBits(radius);
        return 31*h+(int) (bits^(bits>>>32));
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(').append(' ');
        buf.append('(').append(' ');
        for(double c: coords) { buf.append(c).append(' '); }
        buf.append(')');
        buf.append(" @ ").append(radius);
        buf.append(' ').append(')');
        return buf.toString();
    }

}
