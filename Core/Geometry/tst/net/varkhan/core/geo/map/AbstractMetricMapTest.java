package net.varkhan.core.geo.map;

import junit.framework.TestCase;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.core.geo.shape.d2.rec.PointD2D;
import net.varkhan.core.geo.shape.d2.Rect2D;
import net.varkhan.core.geo.shape.d2.Shape2D;

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

    protected Shape2D getPoint(double x, double y) { return new PointD2D(x, y);}

    protected void testClosest(Random rand, Rect2D bounds, MetricMap<Shape2D, String> geomap, long size) {
        double xmin = bounds.xmin();
        double xmax = bounds.xmax();
        double ymin = bounds.ymin();
        double ymax = bounds.ymax();
        System.err.println("Testing Closest on "+ size +" points in "+ bounds);
        Map<String,Shape2D> locs = new HashMap<String, Shape2D>();
        for(int i=0; i<size; i++) {
            double x = xmin+(xmax-xmin)*rand.nextDouble();
            double y = ymin+(ymax-ymin)*rand.nextDouble();
            String n = "Point-"+i+"-("+x+","+y+")";
            final Shape2D c = getPoint(x, y);
            locs.put(n, c);
            geomap.add(c,n);
            assertEquals("size() @" + n, locs.size(), geomap.size());
        }
//        System.err.println(geomap);
        for(Map.Entry<String,Shape2D> e: locs.entrySet()) {
            String t = e.getKey();
            Shape2D c = e.getValue();
            double x = c.xctr();
            double y = c.yctr();
            System.err.println(t+"\t=>\t"+findAll(locs, x, y));
            assertTrue(t, findAllKeys(locs, x, y).contains(t));
            assertEquals("findClosest() @[" + x + "," + y + "]", findClosest(locs, x, y), geomap.get(x, y, Double.MAX_VALUE));
        }
        for(int i=0; i<size; i++) {
            double x = xmin+(xmax-xmin)*rand.nextDouble();
            double y = ymin+(ymax-ymin)*rand.nextDouble();
            final String closest = findClosest(locs, x, y);
            final String all = geomap.get(x, y, Double.MAX_VALUE);
            assertEquals("findClosest() #"+i+"-["+x+","+y+"]", closest, all);
        }
    }

    protected void testSearch(Random rand, Rect2D bounds, MetricMap<Shape2D,String> geomap, long size, double prec) {
        double xmin = bounds.xmin();
        double xmax = bounds.xmax();
        double ymin = bounds.ymin();
        double ymax = bounds.ymax();
        System.err.println("Testing Search on "+ size +" points in "+ bounds);
        Map<String,Shape2D> locs = new HashMap<String, Shape2D>();
        for(int i=0; i<size; i++) {
            double x = xmin+(xmax-xmin)*rand.nextDouble();
            double y = ymin+(ymax-ymin)*rand.nextDouble();
            String n = "Point-"+i+"-("+x+","+y+")";
            final Shape2D c = getPoint(x, y);
            locs.put(n, c);
            geomap.add(c,n);
            assertEquals("size() @" + n, locs.size(), geomap.size());
        }
//        System.err.println(geomap);
        for(Map.Entry<String,Shape2D> e: locs.entrySet()) {
            String t = e.getKey();
            Shape2D c = e.getValue();
            double x = c.xctr();
            double y = c.yctr();
            assertTrue(t, findAllKeys(locs, x, y).contains(t));
            System.err.println(t);
            assertEquals("findClosest() @[" + x + "," + y + "]", findAll(locs, x, y), geomap.getAll(x, y, prec));
        }
        for(int i=0; i<size; i++) {
            double x = xmin+(xmax-xmin)*rand.nextDouble();
            double y = ymin+(ymax-ymin)*rand.nextDouble();
            final net.varkhan.base.containers.map.Map<Shape2D,String> closest = findAll(locs, x, y);
            final net.varkhan.base.containers.map.Map<Shape2D,String> all = geomap.getAll(x, y, 0);
            assertEquals("findClosest() #"+i+"-["+x+","+y+"]", closest, all);
        }
    }

    protected void bmarkClosest(Random rand, Rect2D bounds, MetricMap<Shape2D,String> geomap, long size, int count, int depth, double prec) {
        double xmin = bounds.xmin();
        double xmax = bounds.xmax();
        double ymin = bounds.ymin();
        double ymax = bounds.ymax();
        testSearch(rand, bounds, geomap, size, prec);
        List<double[]> points = new ArrayList<double[]>(count);
        for(int i=0; i< count; i++) {
            double x = xmin+(xmax-xmin)* rand.nextDouble();
            double y = ymin+(ymax-ymin)* rand.nextDouble();
            points.add(new double[]{x,y,i});
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
            double x = p[0];
            double y = p[1];
            final String all = geomap.get(x, y, Double.MAX_VALUE);
            final int i = (int) p[2];
            assertNotNull(/*""+i+" ("+x+","+y+")",*/all);
        }
        t1 = System.nanoTime();
        System.out.println("MX: Fetched "+ count +" locations in "+((t1-t0)/1000)+"us, "+((t1-t0)/ count)+"ns/elt on "+ size +" entries");
    }

    private <T> Set<T> findAllKeys(Map<T, Shape2D> m, double x, double y) {
//        double dm = Double.MAX_VALUE;
        Set<T> tm = new HashSet<T>();
        for(Map.Entry<T,Shape2D> e: m.entrySet()) {
            T t = e.getKey();
            Shape2D c = e.getValue();
//            double dx = c.xctr()-x;
//            double dy = c.yctr()-y;
//            double dc = dx*dx+dy*dy;
            if(c.contains(x,y)) {
//                dm = dc;
                tm.add(t);
            }
        }
        return tm;
    }

    private <T> Set<Shape2D> findAllVals(Map<T, Shape2D> m, double x, double y) {
//        double dm = Double.MAX_VALUE;
        Set<Shape2D> tm = new HashSet<Shape2D>();
        for(Map.Entry<T,Shape2D> e: m.entrySet()) {
            T t = e.getKey();
            Shape2D c = e.getValue();
//            double dx = c.xctr()-x;
//            double dy = c.yctr()-y;
//            double dc = dx*dx+dy*dy;
            if(c.contains(x,y)) {
//                dm = dc;
                tm.add(c);
            }
        }
        return tm;
    }


    private <T> net.varkhan.base.containers.map.Map<Shape2D,T> findAll(Map<T, Shape2D> m, double x, double y) {
//        double dm = Double.MAX_VALUE;
        net.varkhan.base.containers.map.Map<Shape2D,T> tm = new ArrayOpenHashMap<Shape2D,T>();
        for(Map.Entry<T,Shape2D> e: m.entrySet()) {
            T t = e.getKey();
            Shape2D c = e.getValue();
//            double dx = c.xctr()-x;
//            double dy = c.yctr()-y;
//            double dc = dx*dx+dy*dy;
            if(c.contains(x,y)) {
//                dm = dc;
                tm.add(c,t);
            }
        }
        return tm;
    }

    private <T> T findClosest(Map<T, Shape2D> m, double x, double y) {
        double dm = Double.MAX_VALUE;
        T tm = null;
        for(Map.Entry<T,Shape2D> e: m.entrySet()) {
            T t = e.getKey();
            Shape2D c = e.getValue();
            double dx = c.xctr()-x;
            double dy = c.yctr()-y;
            double dc = dx*dx+dy*dy;
            if(dc<dm) {
                tm = t;
                dm = dc;
            }
        }
        return tm;
    }
}
