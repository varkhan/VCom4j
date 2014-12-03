package net.varkhan.core.geo.map;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.map.Map;
import net.varkhan.core.geo.shape.Point;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 7:27 PM
 */
public interface MetricMap<K,V> extends Container<Map.Entry<K,V>>, Map<K,V> {

    public long size();

    public boolean isEmpty();

    public void clear();

    public boolean has(K key);

    public boolean has(K key, double rad);

    public boolean has(Point p, double r);

    public boolean has(double[] c, double r);

    public V get(K key);

    public V get(Point p);

    public V get(double[] c);

    public V get(K key, double rad);

    public V get(Point p, double r);

    public V get(double[] c, double r);

    public Map<K,V> getAll(K key, double rad);

    public Map<K,V> getAll(Point p, double r);

    public Map<K,V> getAll(double[] c, double r);

    public boolean add(K k, V val);

    public <Par> long visit(Visitor<Map.Entry<K,V>,Par> vis, Par par);

    public <Par> long visit(Map.MapVisitor<K,V,Par> vis, Par par);

    public <Par> long visit(Visitor<Map.Entry<K,V>,Par> vis, K key, double rad, Par par);

    public <Par> long visit(Map.MapVisitor<K,V,Par> vis, K key, double rad, Par par);

}
