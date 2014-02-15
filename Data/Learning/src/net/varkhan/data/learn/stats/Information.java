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
public class Information<A,C> extends AbstractPurity<A,C> {

    protected static final double INVLOG2 = 1.0/Math.log(2);

    public double invoke(Collection<CountingSet<A>> parts, CountingSet<A> all, C ctx) {
        return 1-entropy(parts);
    }

    protected static <A> double entropy(Collection<CountingSet<A>> parts) {
        double num = 0;
        double ent = 0;
        for(CountingSet<A> set: parts) {
            double c = set.count();
            num += c;
            ent += c * Math.log(c);
        }
        if(num==0) return 0;
        return INVLOG2 * (Math.log(num) - ent/num);
    }

}
