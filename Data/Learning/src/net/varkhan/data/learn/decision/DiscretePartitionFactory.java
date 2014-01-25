package net.varkhan.data.learn.decision;

import net.varkhan.base.containers.map.ArrayOpenHashObj2LongMap;
import net.varkhan.base.containers.map.Obj2LongMap;
import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;
import net.varkhan.base.functor.curry.Pair;
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

    public Partition<A,T,C> invoke(Collection<Pair<T,K>> obs, C ctx) {
        Map<K,Collection<Pair<A,K>>> classes=new HashMap<K,Collection<Pair<A,K>>>();
        for(Pair<T,K> o : obs) {
            K k=o.rvalue();
            Collection<Pair<A,K>> g=classes.get(k);
            if(g==null) classes.put(k, g=new HashSet<Pair<A,K>>());
            g.add(new Pair.Value<A,K>(attr.invoke(o.lvalue(), ctx),k));
        }
        List<Collection<Pair<A,K>>> parts=new ArrayList<Collection<Pair<A,K>>>(classes.values());
        Collections.sort(parts, LARGEST);
        Collection<Collection<K>> bags = new ArrayList<Collection<K>>();
        Collection<K> def = new ArrayList<K>();
        final Obj2LongMap<A> index = new ArrayOpenHashObj2LongMap<A>(bags.size());
        long idx = card;
        for(Collection<Pair<A,K>> part: parts) {
            if(idx>1) {
                idx --;
                Collection<K> bag = new ArrayList<K>(part.size());
                // Take the card-1 largest parts as main bags, with non-zero indices
                for(Pair<A,K> val : part) {
                    index.add(val.lvalue(), idx);
                    bag.add(val.rvalue());
                }
                bags.add(bag);
            }
            else {
                for(Pair<A,K> val : part) {
                    index.add(val.lvalue(), 0);
                    def.add(val.rvalue());
                }
            }
        }
        bags.add(def);
        return new Partition<A,T,C>(attr,new Ordinal<A,C>() {
            @Override
            public long cardinal() { return card; }

            @Override
            public long invoke(A arg, C ctx) {
                long idx=index.getLong(arg);
                // If 0, default bag or unknown value -- those get index 0
                if(idx<=0) return 0;
                return idx;
            }
        },pure.invoke(bags, ctx));
    }

}
