package net.varkhan.core.geo.map;

import junit.framework.TestCase;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.core.geo.shape.PointD;
import net.varkhan.core.geo.shape.Rect;
import net.varkhan.core.geo.shape.Shape;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 8:19 PM
 */
public abstract class AbstractMetricMapTest extends TestCase {

    protected Shape getPoint(double[] cc) { return new PointD(cc);}

    protected void testClosest(Random rand, Rect bounds, MetricMap<Shape, String> geomap, long size) {
        double[] cmin = bounds.cmin();
        double[] cmax = bounds.cmax();
        System.err.println("Testing Closest on "+ size +" points in "+ bounds);
        Map<String,Shape> locs = new HashMap<String, Shape>();
        for(int i=0; i<size; i++) {
            double[] cc = new double[bounds.dim()];
            for(int d=0; d<bounds.dim(); d++) cc[d]=cmin[d]+(cmax[d]-cmin[d])*rand.nextDouble();
            String n = "Point-"+i+"-("+Arrays.toString(cc)+")";
            final Shape c = getPoint(cc);
            locs.put(n, c);
            geomap.add(c,n);
            assertEquals("size() @" + n, locs.size(), geomap.size());
        }
//        System.err.println(geomap);
        for(Map.Entry<String,Shape> e: locs.entrySet()) {
            String t = e.getKey();
            Shape c = e.getValue();
            double[] cc = c.cctr();
            System.err.println(t+"\t=>\t"+findAll(locs, cc));
            assertTrue(t, findAllKeys(locs, cc).contains(t));
            assertEquals("findClosest() @[" + Arrays.toString(cc) + "]", findClosest(locs, cc), geomap.get(cc, Double.MAX_VALUE));
        }
        for(int i=0; i<size; i++) {
            double[] cc = new double[bounds.dim()];
            for(int d=0; d<bounds.dim(); d++) cc[d]=cmin[d]+(cmax[d]-cmin[d])*rand.nextDouble();
            final String closest = findClosest(locs, cc);
            final String all = geomap.get(cc, Double.MAX_VALUE);
            assertEquals("findClosest() #"+i+"-["+Arrays.toString(cc)+"]", closest, all);
        }
    }

    protected void testSearch(Random rand, Rect bounds, MetricMap<Shape,String> geomap, long size, double prec) {
        double[] cmin = bounds.cmin();
        double[] cmax = bounds.cmax();
        System.err.println("Testing Search on "+ size +" points in "+ bounds);
        Map<String,Shape> locs = new HashMap<String, Shape>();
        for(int i=0; i<size; i++) {
            double[] cc = new double[bounds.dim()];
            for(int d=0; d<bounds.dim(); d++) cc[d]=cmin[d]+(cmax[d]-cmin[d])*rand.nextDouble();
            String n = "Point-"+i+"-("+Arrays.toString(cc)+")";
            final Shape c = getPoint(cc);
            locs.put(n, c);
            geomap.add(c,n);
            assertEquals("size() @" + n, locs.size(), geomap.size());
        }
//        System.err.println(geomap);
        for(Map.Entry<String,Shape> e: locs.entrySet()) {
            String t = e.getKey();
            Shape c = e.getValue();
            double[] cc = c.cctr();
            assertTrue(t, findAllKeys(locs, cc).contains(t));
            System.err.println(t);
            assertEquals("findClosest() @[" + Arrays.toString(cc) + "]", findAll(locs, cc), geomap.getAll(cc, prec));
        }
        for(int i=0; i<size; i++) {
            double[] cc = new double[bounds.dim()];
            for(int d=0; d<bounds.dim(); d++) cc[d]=cmin[d]+(cmax[d]-cmin[d])*rand.nextDouble();
            final net.varkhan.base.containers.map.Map<Shape,String> closest = findAll(locs, cc);
            final net.varkhan.base.containers.map.Map<Shape,String> all = geomap.getAll(cc, 0);
            assertEquals("findClosest() #"+i+"-["+Arrays.toString(cc)+"]", closest, all);
        }
    }

    protected void bmarkClosest(Random rand, Rect bounds, MetricMap<Shape,String> geomap, long size, int count, int depth, double prec) {
        double[] cmin = bounds.cmin();
        double[] cmax = bounds.cmax();
        testSearch(rand, bounds, geomap, size, prec);
        List<double[]> points = new ArrayList<double[]>(count);
        for(int i=0; i< count; i++) {
            double[] cc = new double[1+bounds.dim()];
            cc[0] = i;
            for(int d=0; d<bounds.dim(); d++) cc[1+d]=cmin[d]+(cmax[d]-cmin[d])*rand.nextDouble();
            points.add(cc);
        }
        long t0, t1;
        System.err.println("Benchmarking on "+ count +" calls at size "+ size+", depth "+depth);
//        t0 = System.nanoTime();
//        for(double[] p: points) {
//            double x = p[0];
//            double y = p[1];
//            final String all = findClosest(locs, x, y);
//            assertNotNull(all);
//        }
//        t1 = System.nanoTime();
//        System.out.println("HM: Fetched "+count+" locations in "+((t1-t0)/1000)+"us, "+((t1-t0)/count)+"ns/elt on "+size+" entries");
        t0 = System.nanoTime();
        for(double[] p: points) {
            double[] cc = Arrays.copyOfRange(p,1,p.length);
            final String all = geomap.get(cc, Double.MAX_VALUE);
            final int i = (int) p[0];
            assertNotNull(/*""+i+" ("+x+","+y+")",*/all);
        }
        t1 = System.nanoTime();
        System.out.println("MX: Fetched "+ count +" locations in "+((t1-t0)/1000)+"us, "+((t1-t0)/ count)+"ns/elt on "+ size +" entries");
    }

    private <T> Set<T> findAllKeys(Map<T, Shape> m, double[] cc) {
//        double dm = Double.MAX_VALUE;
        Set<T> tm = new HashSet<T>();
        for(Map.Entry<T,Shape> e: m.entrySet()) {
            T t = e.getKey();
            Shape c = e.getValue();
//            double dx = c.xctr()-x;
//            double dy = c.yctr()-y;
//            double dc = dx*dx+dy*dy;
            if(c.contains(cc)) {
//                dm = dc;
                tm.add(t);
            }
        }
        return tm;
    }

    private <T> Set<Shape> findAllVals(Map<T, Shape> m, double[] cc) {
//        double dm = Double.MAX_VALUE;
        Set<Shape> tm = new HashSet<Shape>();
        for(Map.Entry<T,Shape> e: m.entrySet()) {
            T t = e.getKey();
            Shape c = e.getValue();
//            double dx = c.xctr()-x;
//            double dy = c.yctr()-y;
//            double dc = dx*dx+dy*dy;
            if(c.contains(cc)) {
//                dm = dc;
                tm.add(c);
            }
        }
        return tm;
    }


    private <T> net.varkhan.base.containers.map.Map<Shape,T> findAll(Map<T, Shape> m, double[] cc) {
//        double dm = Double.MAX_VALUE;
        net.varkhan.base.containers.map.Map<Shape,T> tm = new ArrayOpenHashMap<Shape,T>();
        for(Map.Entry<T,Shape> e: m.entrySet()) {
            T t = e.getKey();
            Shape c = e.getValue();
//            double dx = c.xctr()-x;
//            double dy = c.yctr()-y;
//            double dc = dx*dx+dy*dy;
            if(c.contains(cc)) {
//                dm = dc;
                tm.add(c,t);
            }
        }
        return tm;
    }

    private <T> T findClosest(Map<T, Shape> m, double[] cc) {
        double dm = Double.MAX_VALUE;
        T tm = null;
        for(Map.Entry<T,Shape> e: m.entrySet()) {
            T t = e.getKey();
            Shape c = e.getValue();
            double dc = 0.0;
            for(int d=0; d<cc.length; d++) {
                double dv = c.cctr(d)-cc[d];
                dc += dv*dv;
            }
            if(dc<dm) {
                tm = t;
                dm = dc;
            }
        }
        return tm;
    }
}
