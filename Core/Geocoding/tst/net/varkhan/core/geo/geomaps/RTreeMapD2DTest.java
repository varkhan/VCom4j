package net.varkhan.core.geo.geomaps;

import net.varkhan.core.geo.geometry.plane.Rect2D;
import net.varkhan.core.geo.geometry.plane.RectD2D;
import net.varkhan.core.geo.geometry.plane.Shape2D;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 8:18 PM
 */
public class RTreeMapD2DTest extends MetricMapTest {
    private double xmin = 0;
    private double xmax = 100;
    private double ymin = 0;
    private double ymax = 100;

    public void testSearchClosest() {
        long size = 100;
        Random rand = new Random(0);
        final MetricMap<Shape2D, String> mtrie = new RTreeMapD2D<Shape2D,String>(12);
        final Rect2D rect = new RectD2D(xmin, xmax, ymin, ymax);
        testClosest(rand, rect, mtrie, size);
    }

    public void testSearchAll() {
        long size = 100;
        Random rand = new Random(0);
        final MetricMap<Shape2D, String> mtrie = new RTreeMapD2D<Shape2D,String>(12);
        final Rect2D rect = new RectD2D(xmin, xmax, ymin, ymax);
        testSearch(rand, rect, mtrie, size);
    }

    public void testBmrks() {
        bmarkClosest(1000, 1000000,  1);
        bmarkClosest(1000, 1000000,  2);
        bmarkClosest(1000, 1000000,  3);
        bmarkClosest(1000, 1000000,  4);
        bmarkClosest(1000, 1000000,  5);
        bmarkClosest(1000, 1000000,  6);
        bmarkClosest(1000, 1000000,  7);
        bmarkClosest(1000, 1000000,  8);
        bmarkClosest(1000, 1000000,  9);
        bmarkClosest(1000, 1000000, 10);
        bmarkClosest(10000, 100000,  1);
        bmarkClosest(10000, 100000,  2);
        bmarkClosest(10000, 100000,  3);
        bmarkClosest(10000, 100000,  4);
        bmarkClosest(10000, 100000,  5);
        bmarkClosest(10000, 100000,  6);
        bmarkClosest(10000, 100000,  7);
        bmarkClosest(10000, 100000,  8);
        bmarkClosest(10000, 100000,  9);
        bmarkClosest(10000, 100000, 10);
//        bmarkClosest(100000, 10000,  4);
//        bmarkClosest(100000, 10000,  5);
//        bmarkClosest(100000, 10000,  6);
//        bmarkClosest(100000, 10000,  7);
//        bmarkClosest(100000, 10000,  8);
//        bmarkClosest(100000, 10000,  9);
//        bmarkClosest(100000, 10000, 10);
//        bmarkClosest(100000, 10000, 20);
    }

    public void bmarkClosest(long size, int count, int depth) {
        Random rand = new Random(0);
        final MetricMap<Shape2D, String> mtrie = new RTreeMapD2D<Shape2D,String>(12);
        final Rect2D rect = new RectD2D(xmin, xmax, ymin, ymax);
        bmarkClosest(rand,rect,mtrie,size,count,depth);
    }
}
