package net.varkhan.data.learn.decision;

import net.varkhan.base.containers.set.ArrayOpenHashCountingSet;
import net.varkhan.base.containers.set.CountingSet;
import net.varkhan.base.functor._;
import net.varkhan.base.functor.curry.Pair;
import net.varkhan.data.learn.SupervisedLearner;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 12:39 PM
 */
public class DecisionLearner<K,T,C> implements SupervisedLearner<K,T,C> {
    protected final Collection<Partition.Factory<K,?,T,C>> attributes=new HashSet<Partition.Factory<K,?,T,C>>();
    protected final Collection<Pair<T,K>>                  observed  =new ArrayList<Pair<T,K>>();
    protected final DecisionTree<K,T,C>                    tree      =new DecisionTree<K,T,C>();
    protected final double minc;
    protected final long maxd;

    public DecisionLearner(double minc, long maxd, Partition.Factory<K,?,T,C>... attr) {
        this.minc=minc;
        this.maxd=maxd;
        for(Partition.Factory<K,?,T,C> a : attr) this.attributes.add(a);
    }

    public boolean train(Iterable<? extends _<T,? extends _<K,_>>> dat, C ctx) {
        boolean m = false;
        for(_<T,? extends _<K,_>> d: dat) m |= observed.add(new Pair.Value<T,K>(d));
        if(!m) return false;
        DecisionTree.Node<K,?,T,C> l = learnTree(tree.tree, observed, ctx, 0);
        if(l!=null) {
            tree.tree = l;
            return true;
        }
        return false;
    }

    @Override
    public boolean train(T obs, K key, C ctx) {
        if(!observed.add(new Pair.Value<T,K>(obs, key))) return false;
        DecisionTree.Node<K,?,T,C> l = learnPath(tree.tree, observed, obs, ctx, 0);
        if(l!=null) {
            tree.tree = l;
            return true;
        }
        return false;
    }

    public boolean train(C ctx) {
        DecisionTree.Node<K,?,T,C> l = learnTree(tree.tree, observed, ctx, 0);
        if(l!=null) {
            tree.tree = l;
            return true;
        }
        return false;
    }

    /**
     * Algo:
     *  - select an attribute to partition on
     *  - find the best partition for this attribute
     *  - if no attribute could be used or partition is not good enough, create a single leaf with majority key
     *  - otherwise create a root, and for each partitioned set of values, iterate down
     *  - set the node in parent to the created node
     *
     */
    protected DecisionTree.Node<K,?,T,C> learnPath(DecisionTree.Node<K,?,T,C> node, Collection<Pair<T,K>> values, T obs, C ctx, int l) {
        Set<K> classes=classes(values);
        if(classes.size()<=1) {
            if(node!=null && node.isLeaf() && classes.contains(((DecisionTree.Leaf<K,?,T,C>) node).key())) {
//                System.out.println("Preserving leaf at "+l+" for \t"+values+"\n\t"+node);
                return null;
            }
            return subLeaf(values);
        }
//        System.out.println("Learning subtree at "+l+" on "+obs+" for "+values+"\n\t"+node);
        @SuppressWarnings("unchecked")
        Partition<Object,T,C> cand = (Partition<Object, T, C>) optPart(values, ctx);
        if(node!=null) {
            @SuppressWarnings("unchecked")
            Partition<Object,T,C> part = (Partition<Object, T, C>) node.partition();
            if(Partition.identical_(part, cand, values, ctx)) {
//                System.out.println("Preserving subtree at "+l+" for \t"+values+"\n\t"+part);
                if(node.isLeaf()) return null;  // No change!
                long s = part.invoke(obs, ctx);
                @SuppressWarnings("unchecked")
                DecisionTree.Root<K,?,T,C> root = (DecisionTree.Root<K,?,T,C>) node;
                DecisionTree.Node<K,?,T,C> d = root.get(s);
                List<List<Pair<T,K>>> groups = Partition.partition_(part, values, ctx);
                DecisionTree.Node<K,?,T,C> n = learnPath(d, groups.get((int) s), obs, ctx, l+1);
                if(n==null) return null; // No change!
                if(n!=d) root.set(n, s);
                return node;
            }
        }
        return subTree(cand, values, ctx, l);
    }

    protected DecisionTree.Node<K,?,T,C> learnTree(DecisionTree.Node<K,?,T,C> node, Collection<Pair<T,K>> values, C ctx, int l) {
        Set<K> classes=classes(values);
        if(classes.size()<=1) {
            if(node!=null && node.isLeaf() && classes.contains(((DecisionTree.Leaf<K,?,T,C>) node).key())) {
//                System.out.println("Preserving leaf at "+l+" for \t"+values+"\n\t"+node);
                return null;
            }
            return subLeaf(values);
        }
//        System.out.println("Learning subtree at "+l+" for "+values+"\n\t"+node);
        @SuppressWarnings("unchecked")
        Partition<Object,T,C> cand = (Partition<Object, T, C>) optPart(values, ctx);
        if(node!=null) {
            @SuppressWarnings("unchecked")
            Partition<Object,T,C> part = (Partition<Object, T, C>) node.partition();
            if(Partition.identical_(part, cand, values, ctx)) {
                if(node.isLeaf()) return null;  // No change!
                @SuppressWarnings("unchecked")
                DecisionTree.Root<K,?,T,C> root = (DecisionTree.Root<K,?,T,C>) node;
                boolean c = false;
                l ++;
                for(long s=0; s<part.cardinal(); s++) {
                    DecisionTree.Node<K,?,T,C> d = root.get(s);
                    List<List<Pair<T,K>>> groups = Partition.partition_(part, values, ctx);
                    DecisionTree.Node<K,?,T,C> n = learnTree(d, groups.get((int) s), ctx, l);
                    if(n!=null) {
                        c = true;
                        if(n!=d) root.set(n, s);
                    }
                }
                return c?node:null;
            }
            // Fall through to re-learn the whole root
        }
        return subTree(cand, values, ctx, l);
    }

    protected DecisionTree.Node<K,?,T,C> subTree(Partition<?,T,C> part, Collection<Pair<T,K>> values, C ctx, int l) {
        Set<K> classes=classes(values);
        if(classes.size()<=1) {
            return subLeaf(values);
        }
//        System.out.println("Computing subtree at "+l+" for \t"+values+"\n\t"+part);
        if(part==null || part.confidence()<minc || l>maxd || part.cardinal()<=1) {
            return subLeaf(values);
        }
        DecisionTree.Root<K,?,T,C> r = new DecisionTree.Root<K,Object,T,C>((Partition<Object, T, C>) part);
        List<List<Pair<T,K>>> groups = Partition.partition_(part, values, ctx);
        l ++;
        for(int i=0; i<part.cardinal(); i++) {
            List<Pair<T,K>> v=groups.get(i);
            r.set(subTree(optPart(v, ctx), v, ctx, l),i);
        }
        return r;
    }

    protected Set<K> classes(Collection<Pair<T, K>> values) {
        Set<K> c = new HashSet<K>();
        for(Pair<T, K> p : values) {
            c.add(p.rvalue());
        }
        return c;
    }

    protected DecisionTree.Node<K,?,T,C> subLeaf(Collection<Pair<T,K>> values) {
        CountingSet<K> c = new ArrayOpenHashCountingSet<K>();
        for(Pair<T,K> v: values) c.add(v.rvalue());
        K b = null;
        long m = -1;
        for(K k: (Iterable<K>) c) {
            long n=c.count(k);
            if(m<n) {
                m = n;
                b = k;
            }
        }
//        System.out.println("Creating leaf "+b+" for "+values);
        return new DecisionTree.Leaf<K,Object,T,C>(b,m/(double)c.count());
    }

    protected Partition<?,T,C> optPart(Collection<Pair<T,K>> values, C ctx) {
        Partition<?,T,C> part = null;
        for(Partition.Factory<K,?,T,C> a: attributes) {
            Partition<?,T,C> p = a.invoke(values, ctx);
            if(part==null || p.confidence()>part.confidence()) part=p;
        }
        return part;
    }


    @Override
    public DecisionTree<K,T,C> model() {
        return tree;
    }
}
