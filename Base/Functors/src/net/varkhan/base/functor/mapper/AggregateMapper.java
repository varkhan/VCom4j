package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:06 PM
 */
public abstract class AggregateMapper<R,A,C> implements Mapper<R,A,C> {

    protected final Mapper<R,A,C>[] maps;

    public AggregateMapper(Mapper<R,A,C>... maps) {
        this.maps=maps;
    }

    @SuppressWarnings("unchecked")
    public Mapper<R,A,C>[] components() {
        return maps.clone();
    }

    public abstract R invoke(A arg, C ctx);

    protected String toString(String op) {
        StringBuilder buf = new StringBuilder(op);
        buf.append('(');
        boolean f = true;
        for(Mapper<R,A,C> m: maps) {
            if(f) f = false;
            else buf.append(',');
            buf.append(m.toString());
        }
        buf.append(')');
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(this.getClass().getSimpleName());
    }

}
