package net.varkhan.data.learn.stats;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.list.ArrayList;
import net.varkhan.base.containers.list.List;
import net.varkhan.base.containers.set.WeightingSet;

import java.util.Comparator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/31/13
 * @time 2:24 PM
 */
public class Algebra {


    /*********************************************************************************
     **  WeightingSet Algebra
     **/

    @SuppressWarnings("unchecked")
    public static <T> WeightingSet<T> scale(final WeightingSet<T> s, final double d, final double f) {
        return new WeightingSet<T>() {
            public long size() { return s.size(); }
            public boolean isEmpty() { return s.isEmpty(); }
            public double weight() { return d*s.size()+f*s.weight(); }
            public void clear() { s.clear(); }
            public boolean has(T t) { return s.has(t); }
            public double weight(T t) { return d+f*s.weight(t); }
            public boolean add(T t, double w) { return s.add(t, (w-d)/f); }
            public boolean del(T t) { return s.del(t); }
            public Iterator<? extends T> iterator() { return s.iterator(); }
            public <P> long visit(Visitor<T,P> vis, P par) { return s.visit(vis, par); }
            public boolean add(T t) { return s.add(t); }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> WeightingSet<T> inject(WeightingSet<T> w, WeightingSet<T> s, double f) {
        for(T v: (java.lang.Iterable<? extends T>) s) w.add(v, f*s.weight(v));
        return w;
    }

    public static <T> WeightingSet<T> combine(WeightingSet<T> w, WeightingSet<T> s1, double f1, WeightingSet<T> s2, double f2) {
        inject(w, s1, f1);
        inject(w, s2, f2);
        return w;
    }

    @SuppressWarnings("unchecked")
    public static <T> WeightingSet<T> normalize(WeightingSet<T> w, WeightingSet<T> s, WeightingSet<T> n) {
        for(T v: (java.lang.Iterable<? extends T>) s) {
            double wn=n.weight(v);
            if(wn!=0) w.add(v,s.weight(v)/wn);
        }
        return w;
    }

    @SuppressWarnings("unchecked")
    public static <T> WeightingSet<T> truncate(WeightingSet<T> w, WeightingSet<T> s, double min, double max) {
        for(T v: (java.lang.Iterable<? extends T>) s) {
            double sw = s.weight(v);
            if(min<=sw && sw<=max) w.add(v, sw);
        }
        return w;
    }

    @SuppressWarnings("unchecked")
    public static <T> WeightingSet<T> max(WeightingSet<T> w, WeightingSet<T> s1, WeightingSet<T> s2) {
        for(T v: (Iterable<? extends T>) s1) {
            if(s1.weight(v)>s2.weight(v)) w.add(v,s1.weight(v));
        }
        for(T v: (Iterable<? extends T>) s2) {
            if(s1.weight(v)<=s2.weight(v)) w.add(v,s2.weight(v));
        }
        return w;
    }

    @SuppressWarnings("unchecked")
    public static <T> WeightingSet<T> min(WeightingSet<T> w, WeightingSet<T> s1, WeightingSet<T> s2) {
        for(T v: (Iterable<? extends T>) s1) {
            if(s1.weight(v)<s2.weight(v)) w.add(v,s1.weight(v));
        }
        for(T v: (Iterable<? extends T>) s2) {
            if(s1.weight(v)>=s2.weight(v)) w.add(v,s2.weight(v));
        }
        return w;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> tops(final WeightingSet<T> s, int n) {
        Object[] a = new Object[(int)s.size()];
        int i=0;
        for(Object o: s) a[i++] = o;
        Arrays.sort(new Comparator<T>() {
            public int compare(T o1, T o2) { return Double.compare(s.weight(o2), s.weight(o1)); }
        }, (T[])a);
        List<T> l = new ArrayList<T>();
        for(Object o: a) {
            l.add((T) o);
            if(l.size()>=n) break;
        }
        return l;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> lows(final WeightingSet<T> s, int n) {
        Object[] a = new Object[(int)s.size()];
        int i=0;
        for(Object o: s) a[i++] = o;
        Arrays.sort(new Comparator<T>() {
            public int compare(T o1, T o2) { return Double.compare(s.weight(o1), s.weight(o2)); }
        }, (T[])a);
        List<T> l = new ArrayList<T>();
        for(Object o: a) {
            l.add((T) o);
            if(l.size()>=n) break;
        }
        return l;
    }

}
