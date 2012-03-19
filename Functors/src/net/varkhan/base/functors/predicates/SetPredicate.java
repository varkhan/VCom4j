package net.varkhan.base.functors.predicates;

import net.varkhan.base.functors.Predicate;

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

    private final Set<A> set;

    public SetPredicate(Set<A> set) {
        this.set = set;
    }

    public boolean invoke(A arg, C ctx) {
        return set.contains(arg);
    }
}
