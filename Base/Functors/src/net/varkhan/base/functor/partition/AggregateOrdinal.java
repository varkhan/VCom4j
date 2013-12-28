package net.varkhan.base.functor.partition;

import net.varkhan.base.functor.Ordinal;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:13 PM
 */
public abstract class AggregateOrdinal<A,C> implements Ordinal<A,C> {

    protected final long           card;
    protected final Ordinal<A,C>[] ords;

    public AggregateOrdinal(long card, Ordinal<A,C>... ords) {
        this.card=card;
        this.ords=ords;
    }

    public Ordinal<A,C>[] components() {
        return ords.clone();
    }

    public long cardinal() {
        return card;
    }

    public abstract long invoke(A arg, C ctx);

    public <A,C> AggregateOrdinal<A,C> prod(Ordinal<A,C>... ords) {
        long c = 1;
        for(Ordinal<A,C> p: ords) {
            c *= p.cardinal();
        }
        return new AggregateOrdinal<A,C>(c) {
            public long invoke(A arg, C ctx) {
                long i = 0;
                for(Ordinal<A,C> p: ords) {
                    i = p.cardinal()*i+p.invoke(arg, ctx);
                }
                return i;
            }
        };
    }

}
