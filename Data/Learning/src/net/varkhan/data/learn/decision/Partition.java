package net.varkhan.data.learn.decision;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;
import net.varkhan.base.functor.curry.Pair;
import net.varkhan.base.functor.mapper.ConstMapper;
import net.varkhan.base.functor.ordinal.ConstOrdinal;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 12:47 PM
 */
public class Partition<A,T,C> implements Ordinal<T,C> {

    protected final Mapper<A,T,C> attr;
    protected final Ordinal<A,C>  part;
    protected final double conf;

    public Partition(Mapper<A,T,C> attr, Ordinal<A,C> part, double conf) {
        this.attr=attr;
        this.part=part;
        this.conf=conf;
    }

    public long cardinal() { return part.cardinal(); }
    public long invoke(T arg, C ctx) { return part.invoke(attr.invoke(arg,ctx),ctx); }
    public Mapper<A,T,C> attribute() { return attr; }
    public Ordinal<A,C> partition() { return part; }
    public double confidence() { return  conf; }

    protected static final Partition<Object,Object,Object> NULL=new Partition<Object,Object,Object>(ConstMapper.NULL(), ConstOrdinal.UNITY(), 1);

    @SuppressWarnings("unchecked")
    public static <A,T,C> Partition<A,T,C> NULL() { return (Partition<A,T,C>) NULL; }

    public static interface Factory<K,A,T,C> extends Mapper<Partition<A,T,C>,Collection<Pair<T,K>>,C>{}

}
