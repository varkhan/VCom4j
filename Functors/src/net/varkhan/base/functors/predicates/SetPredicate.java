package net.varkhan.base.functors.predicates;

import net.varkhan.base.functors.Predicate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:15 PM
 */
public class SetPredicate<A,C> implements Predicate<A,C> {

    protected final Set<A> set;

    public SetPredicate(A... vals) {
        this.set = new HashSet<A>(vals.length);
        for(A a: vals) this.set.add(a);
    }

    public SetPredicate(Iterator<A> vals) {
        this.set = new HashSet<A>();
        while(vals.hasNext()) this.set.add(vals.next());
    }

    public SetPredicate(Set<A> set) {
        this.set = set;
    }

    public boolean invoke(A arg, C ctx) {
        return set.contains(arg);
    }
}
