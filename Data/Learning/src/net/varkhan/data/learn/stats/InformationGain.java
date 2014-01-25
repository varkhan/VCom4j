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
public class InformationGain<A,C> implements Purity<A,C> {

    protected static final double INVLOG2 = 1.0/Math.log(2);

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
        return entropy(all)-entropyCond(parts);
    }

    protected static <A> double entropyCond(Collection<CountingSet<A>> parts) {
        double num = 0;
        double ent = 0;
        for(CountingSet<A> set: parts) {
            double c = set.count();
            num += c;
            ent += c * entropy(set);
        }
        if(num==0) return 0;
        return ent/num;
    }

    @SuppressWarnings("unchecked")
    protected static <A> double entropy(CountingSet<A> set) {
        double num = set.count();
        if(num==0) return 0;
        double ent = 0;
        for(A val: (Iterable<? extends A>) set) {
            double p = set.count(val)/num;
            ent -= p * Math.log(p);
        }
        return INVLOG2 * ent;
    }

}
