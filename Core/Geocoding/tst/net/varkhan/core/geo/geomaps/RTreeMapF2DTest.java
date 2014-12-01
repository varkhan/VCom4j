package net.varkhan.core.geo.geomaps;

import net.varkhan.core.geo.geometry.d2.Rect2D;
import net.varkhan.core.geo.geometry.d2.Shape2D;
import net.varkhan.core.geo.geometry.d2.rec.*;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 8:18 PM
 */
public class RTreeMapF2DTest extends AbstractMetricMapTest {
    private double xmin = 0;
    private double xmax = 100;
    private double ymin = 0;
    private double ymax = 100;

    protected Shape2D getPoint(double x, double y) { return new PointF2D(x, y);}


    public void testSearchClosest() {
        long size = 100;
        Random rand = new Random(0);
        final MetricMap<Shape2D, String> mtrie = new RTreeMapF2D<Shape2D,String>(12);
        final Rect2D rect = new RectD2D(xmin, xmax, ymin, ymax);
        testClosest(rand, rect, mtrie, size);
    }

    public void testSearchAll() {
        long size = 100;
        Random rand = new Random(0);
        final MetricMap<Shape2D, String> mtrie = new RTreeMapF2D<Shape2D,String>(12);
        final Rect2D rect = new RectD2D(xmin, xmax, ymin, ymax);
        testSearch(rand, rect, mtrie, size, Float.MIN_NORMAL);
    }

}
