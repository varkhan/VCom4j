package net.varkhan.core.geo.map;

import net.varkhan.core.geo.shape.Rect;
import net.varkhan.core.geo.shape.rec.RectD;
import net.varkhan.core.geo.shape.Shape;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 8:18 PM
 */
public class RTreeMapDTest extends AbstractMetricMapTest {
    private double xmin = 0;
    private double xmax = 100;
    private double ymin = 0;
    private double ymax = 100;
    private double zmin = 0;
    private double zmax = 100;

    public void testSearchClosest() {
        long size = 100;
        Random rand = new Random(0);
        final MetricMap<Shape, String> mtrie = new RTreeMapD<Shape,String>(12);
        final Rect rect = new RectD(new double[]{xmin, ymin, zmin}, new double[]{xmax, ymax, zmax});
        testClosest(rand, rect, mtrie, size);
    }

    public void testSearchAll() {
        long size = 100;
        Random rand = new Random(0);
        final MetricMap<Shape, String> mtrie = new RTreeMapD<Shape,String>(12);
        final Rect rect = new RectD(new double[]{xmin, ymin, zmin}, new double[]{xmax, ymax, zmax});
        testSearch(rand, rect, mtrie, size, Double.MIN_NORMAL);
    }

}
