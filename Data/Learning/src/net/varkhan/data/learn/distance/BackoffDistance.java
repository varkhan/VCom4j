package net.varkhan.data.learn.distance;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.set.WeightingSet;
import net.varkhan.base.functor.Functional;

import java.util.HashSet;
import java.util.Set;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/7/13
 * @time 6:03 PM
 */
public class BackoffDistance<T,C> extends Distance<WeightingSet<T>,C> {
    protected final Functional<T,C> bkf;
    protected final Distance<WeightingSet<T>,C> dst;

    public BackoffDistance(Functional<T,C> bkf, Distance<WeightingSet<T>,C> dst) {
        this.bkf=bkf;
        this.dst=dst;
    }

    @Override
    @SuppressWarnings("unchecked")
    public double invoke(WeightingSet<T> lvalue, WeightingSet<T> rvalue, C ctx) {
        Set<T> k = new HashSet<T>();
        for(T t: (Iterable<T>)lvalue) {
            k.add(t);
        }
        for(T t: (Iterable<T>)rvalue) {
            k.add(t);
        }
        double bkw = 0;
        for(T t: k) bkw += bkf.invoke(t, ctx);
        return dst.invoke(new BackoffSet<T, C>(lvalue, bkf, bkw, ctx), new BackoffSet<T, C>(rvalue, bkf, bkw, ctx), ctx);
    }

    protected static class BackoffSet<T,C> implements WeightingSet<T> {
        protected final Functional<T,C> bkf;
        protected final WeightingSet<T> obs;
        protected final C               ctx;
        protected final double          bkw;

        public BackoffSet(WeightingSet<T> obs, Functional<T,C> bkf, double bkw, C ctx) {
            this.bkf=bkf;
            this.obs=obs;
            this.ctx=ctx;
            this.bkw=bkw;
        }

        public long size() { return obs.size(); }
        public boolean isEmpty() { return obs.isEmpty(); }
        public double weight() { return bkw+obs.weight(); }
        public boolean has(T t) { return obs.has(t); }
        public double weight(T t) { return bkf.invoke(t, ctx)+obs.weight(t); }
        public void clear() { }
        public boolean add(T t) { return false; }
        public boolean add(T t, double wgh) { return false; }
        public boolean del(T t) { return false; }
        public Iterator<? extends T> iterator() { return obs.iterator(); }
        public <Par> long visit(Visitor<T,Par> vis, Par par) { return obs.visit(vis, par); }

    }


}
