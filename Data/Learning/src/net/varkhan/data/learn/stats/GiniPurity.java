package net.varkhan.data.learn.stats;

import net.varkhan.base.containers.set.ArrayOpenHashCountingSet;
import net.varkhan.base.containers.set.CountingSet;

import java.util.ArrayList;
import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 3:20 PM
 */
public class GiniPurity<A,C> implements Purity<A,C> {

    @Override
    public double invoke(Collection<? extends Collection<A>> arg, C ctx) {
        CountingSet<A> all = new ArrayOpenHashCountingSet<A>();
        Collection<CountingSet<A>> parts = new ArrayList<CountingSet<A>>();
        for(Collection<A> bag: arg) {
            CountingSet<A> set = new ArrayOpenHashCountingSet<A>();
            for(A val: bag) {
                set.add(val);
                all.add(val);
            }
            parts.add(set);
        }
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
