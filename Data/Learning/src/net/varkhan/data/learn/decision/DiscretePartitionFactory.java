package net.varkhan.data.learn.decision;

import net.varkhan.base.containers.map.ArrayOpenHashObj2LongMap;
import net.varkhan.base.containers.map.Obj2LongMap;
import net.varkhan.base.containers.set.ArrayOpenHashCountingSet;
import net.varkhan.base.containers.set.CountingSet;
import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;
import net.varkhan.base.functor.curry.Pair;
import net.varkhan.base.functor.mapper.ConstMapper;
import net.varkhan.base.functor.ordinal.ConstOrdinal;
import net.varkhan.data.learn.stats.Purity;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 1:33 PM
 */
public class DiscretePartitionFactory<K,A,T,C> implements Partition.Factory<K,A,T,C> {
    protected static final Comparator<Collection> LARGEST=new Comparator<Collection>() {
        @Override
        public int compare(Collection o1, Collection o2) {
            if(o1.size()>o2.size()) return -1;
            if(o1.size()<o2.size()) return +1;
            return 0;
        }
    };
    protected final Mapper<A,T,C> attr;
    protected final long          card;
    protected final Purity<K,C>   pure;

    public DiscretePartitionFactory(Mapper<A,T,C> attr, long card, Purity<K,C> pure) {
        this.attr=attr;
        this.card=card;
        this.pure=pure;
    }

    public Partition<A,T,C> invoke(Collection<? extends Pair<T,K>> obs, C ctx) {
        Map<A,Collection<Pair<A,K>>> classes=new HashMap<A,Collection<Pair<A,K>>>();
        for(Pair<T,K> o : obs) {
            K k=o.rvalue();
            T t=o.lvalue();
            A a=attr.invoke(t, ctx);
            Collection<Pair<A,K>> g=classes.get(a);
            if(g==null) classes.put(a, g=new HashSet<Pair<A,K>>());
            g.add(new Pair.Value<A,K>(a,k));
        }
//        if(classes.size()<=1) return new Partition<A,T,C>((Mapper<A,T,C>) ConstMapper.NULL(), (Ordinal<A,C>) ConstOrdinal.UNITY(), 1.0);
        List<Collection<Pair<A,K>>> parts=new ArrayList<Collection<Pair<A,K>>>(classes.values());
        Collections.sort(parts, LARGEST);

        CountingSet<K> all = new ArrayOpenHashCountingSet<K>();
        Collection<CountingSet<K>> sets = new ArrayList<CountingSet<K>>();
        CountingSet<K> def = new ArrayOpenHashCountingSet<K>();
        final Obj2LongMap<A> index = new ArrayOpenHashObj2LongMap<A>(parts.size());
        long idx = card;
        if(idx>parts.size()) idx = parts.size();
        final long card = idx;
        for(Collection<Pair<A,K>> part: parts) {
            idx --;
            if(idx>0) {
                CountingSet<K> set = new ArrayOpenHashCountingSet<K>();
                // Take the card-1 largest parts as main bags, with non-zero indices
                for(Pair<A,K> val : part) {
                    index.add(val.lvalue(), idx);
                    set.add(val.rvalue());
                    all.add(val.rvalue());
                }
                sets.add(set);
            }
            // For all other parts (indices 0 and <0) we add to the default bag
            else {
                CountingSet<K> set = new ArrayOpenHashCountingSet<K>();
                for(Pair<A,K> val : part) {
                    index.add(val.lvalue(), 0);
                    set.add(val.rvalue());
                    def.add(val.rvalue());
                    all.add(val.rvalue());
                }
            }
        }
        sets.add(def);
        double conf = pure.invoke(sets, all, ctx);
//        System.out.println("Partitions "+card+" / "+parts.size()+" at "+conf+" on "+attr+" for \t"+obs+"\n\t"+sets);
        return new Partition<A,T,C>(
                attr,
                new Ordinal<A,C>() {
                    @Override
                    public long cardinal() { return card; }

                    @Override
                    public long invoke(A arg, C ctx) {
                        long idx=index.getLong(arg);
                        // If 0, default bag or unknown value -- those get index 0
                        if(idx<=0) return 0;
                        return idx;
                    }

                    @Override
                    public String toString() {
                        StringBuilder buf = new StringBuilder();
                        buf.append('{');
                        boolean f = true;
                        for(Map.Entry<A,Long> e: (Iterable<? extends Map.Entry<A,Long>>) index) {
                            if(f) { f = false; buf.append(' '); }
                            else buf.append(", ");
                            buf.append(e.getKey()).append(": ").append(e.getValue());
                        }
                        buf.append('}');
                        return buf.toString();
                    }
                },
                conf
        );
    }

}
