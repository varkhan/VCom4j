package net.varkhan.data.learn.stats;

import net.varkhan.base.functor.Functional;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 2:59 PM
 */
public interface Purity<A,C> extends Functional<Collection<? extends Collection<A>>,C> {

    public double invoke(Collection<? extends Collection<A>> arg, C ctx);

}
