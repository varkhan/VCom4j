package net.varkhan.core.geometry.plane;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/23/12
 * @time 12:16 PM
 */
public class PolyD2D extends AbstractShape2D {

    protected int num;
    protected double[] xpts;
    protected double[] ypts;

//    r^2 = 1/n * sum(i:(xi-xc)^2+(yi-yc)^2) = 1/n * ( sum(i:xi^2+yi^2) - 2*xc*sum(i:xi) - 2*yc*sum(i:yi) + n*xc^2 + n*yc^2 )
//                                           = 1/n * ( sum(i:xi^2+yi^2) - 2*n*xc^2 - 2*n*yc^2 + n*xc^2 + n*yc^2 )

    protected double x1sm;
    protected double y1sm;
    protected double x2sm;
    protected double y2sm;
    protected double xmin;
    protected double xmax;
    protected double ymin;
    protected double ymax;

    protected void computeInvariants() {
        xmin = ymin = +Double.MAX_VALUE;
        xmax = ymax = -Double.MAX_VALUE;
        x1sm = y1sm = x2sm = y2sm = 0;
        for(int i=0; i<num; i++) {
            double x = xpts[i];
            double y = ypts[i];
            if(xmin>x) xmin=x;
            if(xmax<x) xmax=x;
            if(ymin>y) ymin=y;
            if(ymax<y) ymax=y;
            x1sm += x;
            y1sm += y;
            x2sm += x*x;
            y2sm += y*y;
        }
    }


    public double xmin() { return xmin; }
    public double xmax() { return xmax; }
    public double ymin() { return ymin; }
    public double ymax() { return ymax; }

    @Override
    public double xctr() {
        return x1sm/num;
    }

    @Override
    public double yctr() {
        return y1sm/num;
    }

    @Override
    public Point2D ctr() {
        return new AbstractPoint2D() {
            public double xctr() { return x1sm/num; }
            public double yctr() { return y1sm/num; }
        };
    }

    @Override
    public double rad() {
        return Math.sqrt(rad2());
    }

    @Override
    public double rad2() {
        return (x2sm+y2sm-x1sm*x1sm-y1sm*y1sm)/num;
    }

    @Override
    public double msr() {
        if(num<=1) return 0;
        double x = xpts[0];
        double y = ypts[0];
        double x1 = xpts[0];
        double y1 = ypts[0];
        double a = 0;
        for(int i=1; i<num; i++) {
            double x2 = xpts[i];
            double y2 = ypts[i];
            a += area(x,y,x1,y1,x2,y2);
            x1 = x2;
            y1 = y2;
        }
        return a<0? -a:a;
    }

    @Override
    public boolean contains(double x, double y) {
        return (winding(x, y, num, xpts, ypts)&1)!=0;
    }

    @Override
    public double dmin(double x, double y) {
        return Math.sqrt(dmin2(x, y));
    }

    @Override
    public double dmax(double x, double y) {
        return Math.sqrt(dmax2(x, y));
    }

    @Override
    public double dmin2(double x, double y) {
        if((winding(x, y, num, xpts, ypts)&1)!=0) return 0;
        return distmin2(x, y, num, xpts, ypts);
    }

    @Override
    public double dmax2(double x, double y) {
        return distmax2(x, y, num, xpts, ypts);
    }


    public static int winding(double x, double y, int num, double[] xpts, double[] ypts) {
        if(num<2) return 0;
        // MacMartin's algorithm
        int nc = 0;
        double x0 = xpts[num-1];
        double y0 = ypts[num-1];
        for(int i=0; i<num; i++) {
            double xi = xpts[i];
            double yi = ypts[i];
            if(y0<=y && yi>y) {
                double dx = xi-x0;
                double dy = yi-y0;
                if(((x0-x)*dy-(y0-y)*dx)*dy>0) nc ++;
            }
            else if(y0>=y && yi<y) {
                double dx = xi-x0;
                double dy = yi-y0;
                if(((x0-x)*dy-(y0-y)*dx)*dy>0) nc --;
            }
            x0 = xi;
            y0 = yi;
        }
        return nc;
    }

    public static double distmin2(double x, double y, int num, double[] xpts, double[] ypts) {
        if(num<1) return Double.MAX_VALUE;
        double x0 = xpts[num-1]-x;
        double y0 = ypts[num-1]-y;
        double d0 = x0*x0+y0*y0;
        double d = d0;
        for(int i=0; i<num; i++) {
            double xi = xpts[i]-x;
            double yi = ypts[i]-y;
            double dx = xi-x0;
            double dy = yi-y0;
            double p0 = x0*dx+y0*dy;
            double pi = xi*dx+yi*dy;
            double di = xi*xi+yi*yi;
            if(p0*pi<0) {
                double dd = dx*dx+dy*dy;
                double z2 = dd+d0+di;
                /** Derived from Heron's formula:
                 * a2 is the square area of the triangle
                 * a2 = dl*di/4
                 * a2 = ((dd+d0+di)^2-2*(dd^2+d0^2+di^2))/16;
                 * => dl = 4*a2/di = ((z2*z2)-2*(dd*dd+d0*d0+di*di))/(4*di);
                 **/
                double dl = (0.125*(z2*z2)-0.25*(dd*dd+d0*d0+di*di))/di;
                if(d>dl) d=dl;
            }
            else if(d>di) d = di;
            x0 = xi;
            y0 = yi;
            d0 = di;
        }
        return d;
    }

    public static double distmax2(double x, double y, int num, double[] xpts, double[] ypts) {
        if(num<1) return Double.MAX_VALUE;
        double d = 0;
        for(int i=0; i<num; i++) {
            double xi = xpts[i]-x;
            double yi = ypts[i]-y;
            double di = xi*xi+yi*yi;
            if(d<di) d = di;
        }
        return d;
    }

    public static double area(double x, double y, double x1, double y1, double x2, double y2) {
        x1 -= x;
        y1 -= y;
        x2 -= x;
        y2 -= y;
        double dx = x2-x1;
        double dy = y2-y1;
        double d0 = dx*dx+dy*dy;
        double d1 = x1*x1+y1*y1;
        double d2 = x2*x2+y2*y2;
        double s = d0+d1+d2;
        // Derived from Heron's formula
        double a = s*s - 2 * (d0*d0+d1*d1+d2*d2);
        double d = 0.25 * Math.sqrt(a);
        double z = x1*y2-x2*y1;
        return (z>=0) ? d:-d;
    }

    public static double angle(double x, double y, double x1, double y1, double x2, double y2) {
        x1 -= x;
        y1 -= y;
        x2 -= x;
        y2 -= y;
        double d1 = x1*x1 + y1*y1;
        double d2 = x2*x2 + y2*y2;
        double dp = x1*x2 + y1*y2;
        double a = Math.acos(dp/Math.sqrt(d1 * d2));
        double z = x1*y2-x2*y1;
        return (z>=0) ? a:-a;
    }

}
