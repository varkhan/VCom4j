package net.varkhan.core.geo.map;

import net.varkhan.base.containers.map.Map;
import net.varkhan.core.geo.shape.Point;
import net.varkhan.core.geo.shape.d2.Point2D;
import net.varkhan.core.geo.shape.d2.Shape2D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/2/14
 * @time 4:20 PM
 */
public abstract class AbstractMetricMap2D<K extends Shape2D,V> implements MetricMap2D<K, V> {
    @Override
    public boolean has(Point p, double r) {
        double x = p.dim()>0?p.cctr(0):0.0;
        double y = p.dim()>1?p.cctr(1):0.0;
        return has(x,y,r);
    }

    @Override
    public boolean has(double[] c, double r) {
        double x = c.length>0?c[0]:0.0;
        double y = c.length>1?c[1]:0.0;
        return has(x,y,r);
    }


    @Override
    public boolean has(K key) {
        return has(key.xctr(), key.yctr(), 0);
    }

    @Override
    public boolean has(K key, double rad) {
        return has(key.xctr(),key.yctr(),rad);
    }

    @Override
    public boolean has(Point2D p, double r) {
        return has(p.xctr(),p.yctr(),r);
    }

    @Override
    public V get(Point p) {
        double x = p.dim()>0?p.cctr(0):0.0;
        double y = p.dim()>1?p.cctr(1):0.0;
        return get(x,y,0);
    }

    @Override
    public V get(double[] c) {
        double x = c.length>0?c[0]:0.0;
        double y = c.length>1?c[1]:0.0;
        return get(x,y,0);
    }

    @Override
    public V get(Point p, double r) {
        double x = p.dim()>0?p.cctr(0):0.0;
        double y = p.dim()>1?p.cctr(1):0.0;
        return get(x,y,r);
    }

    @Override
    public V get(double[] c, double r) {
        double x = c.length>0?c[0]:0.0;
        double y = c.length>1?c[1]:0.0;
        return get(x,y,r);
    }

    @Override
    public V get(K key) {
        return get(key.xctr(), key.yctr(), 0);
    }

    @Override
    public V get(K key, double rad) {
        return get(key.xctr(),key.yctr(),rad);
    }

    @Override
    public V get(Point2D p, double r) {
        return get(p.xctr(),p.yctr(),r);
    }

    @Override
    public V get(double x, double y) {
        return get(x, y, 0);
    }

    @Override
    public Map<K,V> getAll(K key, double rad) {
        return getAll(key.xctr(),key.yctr(),rad);
    }

    @Override
    public boolean add(Entry<K,V> item) {
        return add(item.getKey(),item.getValue());
    }

    @Override
    public Map<K,V> getAll(Point p, double r) {
        double x = p.dim()>0?p.cctr(0):0.0;
        double y = p.dim()>1?p.cctr(1):0.0;
        return getAll(x,y,r);
    }

    @Override
    public Map<K,V> getAll(double[] c, double r) {
        double x = c.length>0?c[0]:0.0;
        double y = c.length>1?c[1]:0.0;
        return getAll(x,y,r);
    }

    @Override
    public Map<K,V> getAll(Point2D p, double r) {
        return getAll(p.xctr(),p.yctr(),r);
    }


}
