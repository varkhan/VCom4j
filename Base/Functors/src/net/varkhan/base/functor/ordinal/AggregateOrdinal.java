package net.varkhan.base.functor.ordinal;

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
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                boolean f = true;
                for(Ordinal<A,C> o: ords) {
                    if(f) f = false;
                    else buf.append('x');
                    buf.append(o.toString());
                }
                return buf.toString();
            }
        };
    }

    protected String toString(String op) {
        StringBuilder buf = new StringBuilder(op);
        buf.append('(');
        boolean f = true;
        for(Ordinal<A,C> o: ords) {
            if(f) f = false;
            else buf.append(',');
            buf.append(o.toString());
        }
        buf.append(')');
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(this.getClass().getSimpleName());
    }

}
