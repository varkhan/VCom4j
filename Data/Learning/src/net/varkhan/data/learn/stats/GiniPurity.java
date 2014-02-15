package net.varkhan.data.learn.stats;

import net.varkhan.base.containers.set.CountingSet;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 3:20 PM
 */
public class GiniPurity<A,C> extends AbstractPurity<A,C> {

    public double invoke(Collection<CountingSet<A>> parts, CountingSet<A> all, C ctx) {
        return gini(parts);
    }

    protected static <A> double gini(Collection<CountingSet<A>> parts) {
        double num = 0;
        double ent = 0;
        for(CountingSet<A> set: parts) {
            double c = set.count();
            num += c;
            ent += c * c;
        }
        if(num==0) return 0;
        return ent / (num*num);
    }

}
