package net.varkhan.data.learn.cluster;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor._;
import net.varkhan.data.learn.distance.Distance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/7/13
 * @time 1:18 PM
 */
public abstract class GaussianClustering<K extends GaussianClustering.Cluster<T,C>,T,C> implements Clustering<K,T,C> {
    protected final Distance<T,C>   ds;
    protected       Clusters<K,T,C> cs;

    public GaussianClustering(Functional<_<T,_<T,_>>,C> ds) {
        this(Distance.wrap(ds));
    }

    public GaussianClustering(Distance<T,C> ds) {
        this.ds=ds;
    }

    @Override
    public Distance<T,C> distance() {
        return ds;
    }

    @Override
    public Clusters<K,T,C> model() {
        return cs;
    }

    public static class Cluster<T,C> implements Clustering.Cluster<T,C> {
        protected final Distance<T,C> ds;
        /** The list of allpoints in the cluster */
        protected final List<T> values=new ArrayList<T>();
        /** The minimum of the sum of distances between one point and all other points {@code min(v, distsum(v, ...))} */
        protected       double  mindds=Double.MAX_VALUE;
        /** The arg-min of the sum of distances between one point and all other points {@code argmin(v, distsum(v, ...))} */
        protected       T       center=null;
        /** The sum for all pairs of points of their distance {@code sum(v, distsum(v, ...))} */
        protected       double  sumdds=0;

        public Cluster(Distance<T,C> ds) {
            this.ds=ds;
        }

        protected void add(T val, C ctx) {
            double sd = distsum(val, ctx);
            values.add(val);
            // No previous center?
            if(center==null) {
                center = val;
                mindds = Double.MAX_VALUE;
                sumdds += sd;
            }
            // This total distance is smaller than the previous one
            else if(mindds>sd) {
                center = val;
                mindds = sd;
                sumdds += sd;
            }
            else {
                // This total distance is smaller than the previous one updated by the new point
                double d = ds.invoke(val, center, ctx);
                if(mindds+d>sd) {
                    center = val;
                    mindds = sd;
                    sumdds += sd;
                }
                // OK, not sure so we have to recompute everything
                else update(ctx);
            }
        }

        protected void update(C ctx) {
            double cd = Double.MAX_VALUE;
            double sd = 0;
            T cv = null;
            for(T v : values) {
                double d = distsum(v, ctx);
                sd += d;
                if(cd<d) {
                    cd = d;
                    cv = v;
                }
            }
            center = cv;
            mindds = cd;
            sumdds = sd;
        }

        public double distsum(T val, C ctx) {
            double sd = 0;
            for(T v: values) {
                double d = ds.invoke(val,v,ctx);
                sd += d;
            }
            return sd;
        }

        public double distance(T val, C ctx) {
            if(center==null) return ds.invoke(val,val, ctx);
            return ds.invoke(val,center,ctx);
        }

        @Override
        public T center() {
            return center;
        }

        @Override
        public double confidence(T obs, C ctx) {
            if(center==null) return 0;
            double d = ds.invoke(obs, center, ctx);
            return Math.exp(-d/(2*mindds))/Math.sqrt(2*mindds*Math.PI);
        }

        @Override
        public double diameter(double p, C ctx) {
            if(p>=1) return 0;
            if(p<=0) return Double.MAX_VALUE;
            return -(2*mindds)*Math.log(p*Math.sqrt(2*mindds*Math.PI));
        }
    }


    public static class Clusters<K extends Cluster<T,C>,T,C> implements Clustering.Clusters<K,T,C> {

        protected final List<K> cset = new ArrayList<K>();

        protected Clusters() { }

        protected Clusters(Collection<K> cls) { cset.addAll(cls); }

        @Override
        public Collection<K> clusters() {
            return Collections.unmodifiableCollection(cset);
        }

        public void add(K c) {
            cset.add(c);
        }

        public int size() {
            return cset.size();
        }

        @Override
        public K invoke(T obs, C ctx) {
            K cm = null;
            double dm = Double.MAX_VALUE;
            for(K c: cset) {
                double d = c.distance(obs, ctx);
                if(dm>d) {
                    dm = d;
                    cm = c;
                }
            }
            return cm;
        }

        @Override
        public double confidence(K cls, T obs, C ctx) {
            return cls.confidence(obs, ctx);
        }

    }
}
