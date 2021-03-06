package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;

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

    public SetPredicate(Iterable<A> vals) {
        this.set = new HashSet<A>();
        for(A v: vals) this.set.add(v);
    }

    public SetPredicate(Set<A> set) {
        this.set = set;
    }

    public boolean invoke(A arg, C ctx) {
        return set.contains(arg);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("~(");
        boolean f = true;
        for(A v: set) {
            if(f) f = false;
            else buf.append(',');
            buf.append(v);
        }
        return buf.toString();
    }
}
