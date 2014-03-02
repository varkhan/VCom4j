package net.varkhan.data.learn.decision;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;
import net.varkhan.base.functor._;
import net.varkhan.base.functor.curry.Pair;
import net.varkhan.base.functor.mapper.ConstMapper;
import net.varkhan.base.functor.ordinal.ComposedOrdinal;
import net.varkhan.base.functor.ordinal.ConstOrdinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 12:47 PM
 */
public class Partition<A,T,C> extends ComposedOrdinal<T,C> {

    protected final double        conf;

    public Partition(Mapper<A,T,C> mapr, Ordinal<A,C> part, double conf) {
        super(part, mapr);
        this.conf=conf;
    }

    @SuppressWarnings("unchecked")
    public Mapper<A,T,C> attribute() { return (Mapper<A, T, C>) mapr; }

    @SuppressWarnings("unchecked")
    public Ordinal<A,C> partition() { return (Ordinal<A, C>) ordn; }

    public double confidence() { return conf; }

    protected static final Partition<Object,Object,Object> NULL=new Partition<Object,Object,Object>(ConstMapper.NULL(), ConstOrdinal.UNITY(), 1);

    @Override
    public String toString() {
        return super.toString()+"@"+conf;
    }

    @SuppressWarnings("unchecked")
    public static <A,T,C> Partition<A,T,C> NULL() { return (Partition<A,T,C>) NULL; }

    public static interface Factory<K,A,T,C> extends Mapper<Partition<A,T,C>,Iterable<? extends Pair<T,K>>,C>{

    }

    @SuppressWarnings("unchecked")
    public static <A,T,C> List<List<T>> partition(Partition<A,T,C> part, Collection<? extends T> values, C ctx) {
        List<T>[] subs = new List[(int)part.cardinal()];
        for(int i=0; i<subs.length; i++) subs[i] = new ArrayList<T>();
        for(T v: values) {
            long i = part.invoke(v, ctx);
            subs[(int)i].add(v);
        }
        return Arrays.asList(subs);
    }

    @SuppressWarnings("unchecked")
    public static <A,T,C,X extends _<T,?>> List<List<X>> partition_(Partition<A,T,C> part, Collection<? extends X> values, C ctx) {
        List<X>[] subs = new List[(int)part.cardinal()];
        for(int i=0; i<subs.length; i++) subs[i] = new ArrayList<X>();
        for(X v: values) {
            long i = part.invoke(v.lvalue(), ctx);
            subs[(int)i].add(v);
        }
        return Arrays.asList(subs);
    }

    public static <T,C> boolean identical(Partition<?,T,C> part1, Partition<?,T,C> part2, Collection<? extends T> values, C ctx) {
        for(T val: values) if(part1.invoke(val, ctx)!=part2.invoke(val, ctx)) return false;
        return true;
    }

    public static <A,T,C,X extends _<T,?>> boolean identical_(Partition<A,T,C> part1, Partition<A,T,C> part2, Collection<? extends X> values, C ctx) {
        for(X val: values) if(part1.invoke(val.lvalue(), ctx)!=part2.invoke(val.lvalue(), ctx)) return false;
        return true;
    }


}
