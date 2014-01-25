package net.varkhan.data.learn.cluster;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor._;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 7:12 PM
 */
public class KMeansClustering<T,C> extends GaussianClustering<GaussianClustering.Cluster<T,C>,T,C> {

    protected final int cn;
    protected int tr=0;

    public KMeansClustering(Functional<_<T,_<T,_>>,C> ds, int cn) {
        super(ds);
        this.cn=cn;
    }

    public int update(C ctx) {
        int u = 0;
        for(Cluster<T,C> c: cs.clusters()) {
            for(Iterator<T> it=c.values.iterator();it.hasNext();) {
                T p=it.next();
                Cluster<T,C> pc=cs.invoke(p, ctx);
                if(pc!=c) {
                    u++;
                    it.remove();
                    pc.values.add(p);
                }
            }
        }
        if(u>0) for(Cluster<T,C> c: cs.clusters()) c.update(ctx);
        return u;
    }

    @Override
    public boolean train(T obs, C ctx) {
        if(cs.size()<cn) {
            Cluster<T,C> c=new Cluster<T,C>(ds);
            c.add(obs, ctx);
            cs.add(c);
        }
        Cluster<T,C> pc = cs.invoke(obs, ctx);
        pc.add(obs,ctx);
        if(++tr%cn==0) return update(ctx)>0;
        return false;
    }

}
