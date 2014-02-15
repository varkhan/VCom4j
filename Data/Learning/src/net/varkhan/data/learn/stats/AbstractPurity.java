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
 * @date 1/25/14
 * @time 6:52 PM
 */
public abstract class AbstractPurity<A,C> implements Purity<A,C> {

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
        return invoke(parts, all, ctx);
    }

    public abstract double invoke(Collection<CountingSet<A>> parts, CountingSet<A> all, C ctx);

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"(<$>)";
    }
}
