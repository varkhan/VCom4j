package net.varkhan.data.learn.decision;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;
import net.varkhan.base.functor.mapper.ConstMapper;
import net.varkhan.base.functor.ordinal.ConstOrdinal;
import net.varkhan.data.learn.SingletonClassifier;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/26/13
 * @time 7:32 PM
 */
public class DecisionTree<K,T,C> implements Decision.Tree<K,T,C> {
    protected Node<K,?,T,C> tree;

    public Node<K,?,T,C> tree() { return tree; }

    public K invoke(T obs, C ctx) { return tree().invoke(obs, ctx); }
    public double confidence(K key, T obs, C ctx) { return tree().confidence(key, obs, ctx); }
    public Collection<? extends Mapper<?,T,C>> attributes() { return tree().attributes(); }


    public static interface Node<K,A,T,C> extends Decision<K,T,C> {
        public K invoke(T obs, C ctx);
        public double confidence(K key, T obs, C ctx);
        public Collection<? extends Mapper<?,T,C>> attributes();
        public Partition<A,T,C> partition();
        public boolean isLeaf();
        public List<? extends Node<K,?,T,C>> children();
    }

    public static class Leaf<K,A,T,C> extends SingletonClassifier<K,T,C> implements Node<K,A,T,C> {
        protected final Partition<A,T,C> part;
        protected Leaf(K key) { this(key,1); }
        protected Leaf(K key, double cnf) {
            super(key, cnf);
            part = new Partition<A,T,C>((Mapper<A,T,C>) ConstMapper.NULL(), (Ordinal<A,C>) ConstOrdinal.UNITY(), cnf);
        }

        public Collection<? extends Mapper<?,T,C>> attributes() { return Collections.emptySet(); }
        public Partition<A,T,C> partition() { return part; }
        public List<? extends Node<K,?,T,C>> classes() { return Arrays.asList((Node<K, ?, T, C>) this); }
        public boolean isLeaf() { return true; }
        public List<Node<K,?,T,C>> children() { return Arrays.asList(); }
    }

    public static class Root<K,A,T,C> implements Node<K,A,T,C> {
        protected final Partition<A,T,C> part;
        protected final Node<K,?,T,C>[] child;

        @SuppressWarnings("unchecked")
        protected Root(Partition<A,T,C> part) {
            this.part = part;
            this.child = new Node[(int)part.cardinal()];
        }

        protected void child(long s, Node<K,?,T,C> d) {
            child[(int)s] = d;
        }

        public K invoke(T obs, C ctx) {
            long s = part.invoke(obs, ctx);
            if(s>=child.length) return null;
            Node<K,?,T,C> d = child[(int)s];
            return d.invoke(obs, ctx);
        }

        public double confidence(K key, T obs, C ctx) {
            long s = part.invoke(obs, ctx);
            if(s>=child.length) return 0;
            Node<K,?,T,C> d = child[(int)s];
            double c = d.confidence(key, obs, ctx);
            for(Node<K,?,T,C> u: child) {
                if(u!=d) c *= 1-u.confidence(key, obs, ctx);
            }
            return c;
        }

        public Collection<? extends Mapper<?,T,C>> attributes() {
            Set<Mapper<?,T,C>> at = new HashSet<Mapper<?,T,C>>();
            at.add(part.attribute());
            for(Node<K,?,T,C> u: child) at.addAll(u.attributes());
            return at;
        }
        public Partition<A,T,C> partition() { return part; }
        public List<Node<K,?,T,C>> classes() { return Arrays.asList(child); }
        public boolean isLeaf() { return false; }
        public List<Node<K,?,T,C>> children() { return Arrays.asList(child); }
    }

}
